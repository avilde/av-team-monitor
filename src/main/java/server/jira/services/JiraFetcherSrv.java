package server.jira.services;


import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.*;
import com.google.common.collect.Iterables;
import global.L;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import server.jira.model.dao.JiraDao;
import server.jira.model.entity.JrItem;
import server.jira.model.entity.JrProjectUsers;
import server.team.model.dao.TeamsDao;
import server.team.model.entity.TmUserName;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class JiraFetcherSrv {
    private final int ISSUE_BATCH_SIZE = 100;
    @Autowired
    @Qualifier("JiraDao")
    @SuppressWarnings("unused")
    private JiraDao jiraDao;
    @Autowired
    @Qualifier("TeamsDao")
    @SuppressWarnings("unused")
    private TeamsDao teamsDao;
    private List<TmUserName> userNames;
    private List<JrProjectUsers> projectUsers;

    @Scheduled(fixedRate = 600000)
    @SuppressWarnings({"unused", "ResultOfMethodCallIgnored"})
    public void fetchJiraItems() throws Exception {
        if (projectUsers == null)
            projectUsers = jiraDao.getProjectUsers();

        if (userNames == null)
            userNames = teamsDao.getTeamUsers();

        long startTime = System.nanoTime();

        List<CompletableFuture<List<Issue>>> issueFuture = projectUsers
                .stream()
                .map(this::getProjectIssues)
                .collect(Collectors.toList());

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(issueFuture.toArray(new CompletableFuture[0]));

        CompletableFuture<List<Issue>> allIssuesFuture = allFutures.thenApply(v -> issueFuture.stream()
                .map(CompletableFuture::join)
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));

        List<Issue> issues = allIssuesFuture.get();

        Set<Issue> uniqueIssues = new HashSet<>(issues);
        issues.clear();
        issues.addAll(uniqueIssues);

        List<JrItem> items = issues.stream().map(this::transformJiraItem).collect(Collectors.toList());

        items = postFilterItems(items);

        L.LOGGER.info("Total count of tasks: " + items.size());

        jiraDao.deleteAllJiraItems();
        items.forEach(i -> jiraDao.saveJiraItem(i));

        L.LOGGER.info("Service execution time: " + ((System.nanoTime() - startTime) / 1000000) + " ms");
    }

    private List<JrItem> postFilterItems(List<JrItem> items) {
        return items.stream().filter(i ->
                i.getParent() == null ||
                        i.getParent() != null &&
                                !i.getParent().contains("[Recurring]") &&
                                !i.getParent().contains("Sprint Activities")).collect(Collectors.toList());
    }

    private String getUserTeam(String login) {
        for (TmUserName elem : userNames) {
            if (elem.getUserNames().contains(login)) {
                return elem.getTeamName();
            }
        }

        return "";
    }

    private Map<String, String> extractSprintProperties(String result) {
        final String CLOSE_ARRAY_CHARACTER = "]";
        final String COMMA = ",";
        final String EQUALS_SIGN = "=";

        int lastArrayClosure = result.lastIndexOf(CLOSE_ARRAY_CHARACTER);
        result = result.substring(0, lastArrayClosure);
        String[] propertiesArray = result.split(COMMA);
        HashMap<String, String> properties = new HashMap<>();
        for (String property : propertiesArray) {
            String[] keyValueArray = property.split(EQUALS_SIGN);
            if (keyValueArray.length == 1) {
                properties.put(keyValueArray[0], null);
            } else if (keyValueArray.length == 2) {
                properties.put(keyValueArray[0], keyValueArray[1]);
            }
        }
        return properties;
    }

    private CompletableFuture<List<Issue>> getProjectIssues(JrProjectUsers project) {
        return CompletableFuture.supplyAsync(() -> {
            String jqlSearchTemplate = "project = ${project}" +
                    " AND resolution = unresolved" +
                    " AND assignee IN (${users})" +
                    " AND (labels = NULL OR labels NOT IN ('reporting_rfs', 'internal', 'Siebel_Sprint_Activities'))" +
                    " AND summary !~ 'CLONE'" +
                    " AND summary !~ 'Recurring'";
            String formattedJql = jqlSearchTemplate;
            Map<String, String> jqlData;

            Set<String> fields = new HashSet<>();
            fields.add("*all");

            Iterable<Issue> iterableIssues;
            List<Issue> issues = new ArrayList<>();
            int pageSize = ISSUE_BATCH_SIZE;
            int startAt = 0;
            int issuesTotal;
            long startTime = System.nanoTime();
            JiraRestClient client = null;
            try {
                try {
                    client = new JiraConnMgr().getAsyncClient();
                } catch (IOException | URISyntaxException e) {
                    e.getCause();
                }
                assert client != null;
                SearchRestClient searchClient = client.getSearchClient();


                jqlData = new HashMap<>();
                jqlData.put("project", project.getProject());
                jqlData.put("users", project.getUsers());
                formattedJql = StrSubstitutor.replace(jqlSearchTemplate, jqlData);

                L.LOGGER.info("[" + project.getProject() + "] Retrieving batch [0-100 / ?]");
                SearchResult searchResult = searchClient.searchJql(formattedJql, pageSize, startAt, fields).claim();
                issuesTotal = searchResult.getTotal();
                iterableIssues = searchResult.getIssues();
                iterableIssues.forEach(issues::add);

                L.LOGGER.info("[" + project.getProject() + "] Total issue count: " + issuesTotal);

                if (issuesTotal > ISSUE_BATCH_SIZE) {
                    startAt = 101;
                    do {
                        iterableIssues = searchClient.searchJql(formattedJql, pageSize, startAt, fields).claim().getIssues();
                        L.LOGGER.info("[" + project.getProject() + "] Retrieving batch [" + startAt + "-" + (startAt + ISSUE_BATCH_SIZE > issuesTotal ? issuesTotal : startAt + ISSUE_BATCH_SIZE) + " / " + issuesTotal + "]");
                        iterableIssues.forEach(issues::add);

                        startAt += pageSize;
                    } while (startAt <= issuesTotal);
                }

                L.LOGGER.info("[" + project.getProject() + "] Thread execution time: " + ((System.nanoTime() - startTime) / 1000000) + " ms");
            } catch (Exception e) {
                L.LOGGER.error("[" + project.getProject() + "] Jira search client error. Error: " + e.getClass().getSimpleName());
                L.LOGGER.error("[" + project.getProject() + "] Message: " + getShortMessage(e.getMessage()));
                L.LOGGER.error("[" + project.getProject() + "] JQL: " + formattedJql);
                throw new IllegalStateException(e.getCause());
            } finally {
                try {
                    if (client != null) {
                        client.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return issues;
        });
    }

    private String getShortMessage(String message) {
        int lenMsg = message.length();

        if (lenMsg > 150) {
            message = message.substring(0, 150).replaceAll("\n", "");
        }

        return message;
    }

    @SuppressWarnings("ConstantConditions")
    private JrItem transformJiraItem(Issue issue) {
        StringBuilder builder;
        int counter;
        int size;

        JrItem item = new JrItem();
        // setup item
        item.setItemKey(issue.getKey());
        item.setTeam(getUserTeam(issue.getAssignee().getName()));
        try {
            // custom fields
            if (issue.getFieldByName("Actual Result") != null && issue.getFieldByName("Actual Result").getValue() != null)
                item.setActualResult(issue.getFieldByName("Actual Result").getValue().toString());
            if (issue.getFieldByName("Expected Result") != null && issue.getFieldByName("Expected Result").getValue() != null)
                item.setExpectedResult(issue.getFieldByName("Expected Result").getValue().toString());
            if (issue.getFieldByName("Test Data") != null && issue.getFieldByName("Test Data").getValue() != null)
                item.setTestData(issue.getFieldByName("Test Data").getValue().toString());
            if (issue.getFieldByName("Logging") != null && issue.getFieldByName("Logging").getValue() != null)
                item.setLogging(issue.getFieldByName("Logging").getValue().toString());
            if (issue.getFieldByName("Environment Name") != null && issue.getFieldByName("Environment Name").getValue() != null)
                item.setEnvironments(issue.getFieldByName("Environment Name").getValue().toString());
            if (issue.getFieldByName("Reproduce Steps") != null && issue.getFieldByName("Reproduce Steps").getValue() != null)
                item.setReproduceSteps(issue.getFieldByName("Reproduce Steps").getValue().toString());
            // attachments - convert to JSON
            Iterable<Attachment> attachments = issue.getAttachments();
            size = attachments == null ? -1 : Iterables.size(attachments);
            counter = 0;
            builder = new StringBuilder("[");
            if (size > -1) {
                for (Attachment attachment : attachments) {
                    builder
                            .append("{")
                            .append("\"user\": \"").append(attachment.getAuthor().getName()).append("\", ")
                            .append("\"filename\": \"").append(attachment.getFilename()).append("\", ")
                            .append("\"mimeType\": \"").append(attachment.getMimeType()).append("\", ")
                            .append("\"contentUri\": \"").append(attachment.getContentUri()).append("\", ")
                            .append("\"thumbnailUri\": \"").append(attachment.getThumbnailUri()).append("\", ")
                            .append("\"createdDate\": \"").append(attachment.getCreationDate()).append("\", ")
                            .append("\"size\": \"").append(attachment.getSize()).append("\"")
                            .append("}");
                    counter++;
                    if (counter != size) {
                        builder.append(", ");
                    }
                }
            }
            builder.append("]");
            item.setAttachments(builder.toString());
            // attachments - convert to JSON
            Iterable<Comment> comments = issue.getComments();
            size = comments == null ? -1 : Iterables.size(comments);
            counter = 0;
            builder = new StringBuilder("[");
            if (size > -1) {
                for (Comment comment : comments) {
                    builder
                            .append("{")
                            .append("\"id\": \"").append(comment.getId()).append("\", ")
                            .append("\"user\": \"").append(comment.getAuthor().getName()).append("\", ")
                            .append("\"updatedDate\": \"").append(comment.getUpdateDate()).append("\", ")
                            .append("\"body\": \"").append(StringEscapeUtils.escapeJava(comment.getBody())).append("\"")
                            .append("}");
                    counter++;
                    if (counter != size) {
                        builder.append(", ");
                    }
                }
            }
            builder.append("]");
            item.setComments(builder.toString());
            // sprint
            builder = new StringBuilder("[");
            if (issue.getFieldByName("Sprint") != null && issue.getFieldByName("Sprint").getValue() != null) {
                JSONArray sprints = (JSONArray) issue.getFieldByName("Sprint").getValue();

                for (int i = 0; i < sprints.length(); i++) {
                    String sprint = sprints.get(i).toString();
                    Map<String, String> sprintProps = extractSprintProperties(sprint);

                    builder
                            .append("{")
                            .append("\"name\": \"").append(sprintProps.get("name")).append("\", ")
                            .append("\"state\": \"").append(sprintProps.get("state")).append("\"")
                            .append("}");
                    if (i + 1 != sprints.length()) {
                        builder.append(", ");
                    }
                }
            }
            builder.append("]");
            item.setSprints(builder.toString());
            // parent
            if (issue.getFieldByName("Parent") != null && issue.getFieldByName("Parent").getValue() != null) {
                JSONObject parent = (JSONObject) issue.getFieldByName("Parent").getValue();
                String resultString = "{" +
                        "\"itemKey\": \"" + parent.get("key") + "\", " +
                        "\"summary\": \"" + StringEscapeUtils.escapeJava(((JSONObject) parent.get("fields")).get("summary").toString()) + "\"" +
                        "}";
                item.setParent(resultString);
            }
        } catch (Exception e) {
            L.LOGGER.error("cause: " + e.getCause() + ", msg: " + e.getMessage());
        }

        item.setAssignee(issue.getAssignee().getName());
        item.setStatus(issue.getStatus().getName());
        item.setDescription(issue.getDescription());
        item.setSummary(issue.getSummary());
        // issue type
        IssueType issueType = issue.getIssueType();
        item.setType(issueType.getName());
        item.setIsSubtask(issueType.isSubtask());
        item.setPriority(issue.getPriority().getName());
        item.setReporter(issue.getReporter().getName());
        // date fields
        if (issue.getCreationDate() != null)
            item.setCreatedDate(new Timestamp(issue.getCreationDate().getMillis()));
        if (issue.getUpdateDate() != null)
            item.setUpdated(new Timestamp(issue.getUpdateDate().getMillis()));
        if (issue.getDueDate() != null)
            item.setDueDate(new Timestamp(issue.getDueDate().getMillis()));
        // labels
        HashSet<String> labels = (HashSet<String>) issue.getLabels();
        counter = 0;
        builder = new StringBuilder("[");
        for (String label : labels) {
            builder
                    .append("\"").append(label).append("\"");
            counter++;
            if (counter != labels.size()) {
                builder.append(", ");
            }
        }
        builder.append("]");
        item.setLabels(builder.toString());

        return item;
    }
}
