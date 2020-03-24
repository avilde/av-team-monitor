package server.jira.model.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "jr_item", schema = "team_monitor", catalog = "")
public class JrItem {
    private int rowId;
    private String itemKey;
    private String reporter;
    private String assignee;
    private String team;
    private String type;
    private String summary;
    private String description;
    private String priority;
    private String status;
    private Timestamp createdDate;
    private Timestamp updated;
    private Timestamp dueDate;
    private Boolean isSubtask;
    private String labels;
    private String attachments;
    private String testData;
    private String reproduceSteps;
    private String actualResult;
    private String expectedResult;
    private String sprints;
    private String logging;
    private String comments;
    private String environments;
    private String parent;

    @Id
    @Column(name = "row_id", nullable = false)
    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    @Basic
    @Column(name = "jira_key", nullable = false, length = 50)
    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    @Basic
    @Column(name = "reporter", nullable = true, length = 50)
    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    @Basic
    @Column(name = "assignee", nullable = false, length = 50)
    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    @Basic
    @Column(name = "team", nullable = false, length = 50)
    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    @Basic
    @Column(name = "type", nullable = false, length = 50)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "summary", nullable = true, length = 255)
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Basic
    @Column(name = "description", nullable = true, length = -1)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "priority", nullable = true, length = 50)
    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Basic
    @Column(name = "status", nullable = false, length = 50)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "created_date", nullable = false)
    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    @Basic
    @Column(name = "updated_date", nullable = false)
    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    @Basic
    @Column(name = "due_date", nullable = true)
    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    @Basic
    @Column(name = "is_subtask", nullable = false)
    public Boolean getIsSubtask() {
        return isSubtask;
    }

    public void setIsSubtask(boolean isSubtask) {
        this.isSubtask = isSubtask;
    }

    @Basic
    @Column(name = "labels", nullable = true)
    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    @Basic
    @Column(name = "attachments", nullable = true)
    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    @Basic
    @Column(name = "test_data", nullable = true)
    public String getTestData() {
        return testData;
    }

    public void setTestData(String testData) {
        this.testData = testData;
    }

    @Basic
    @Column(name = "reproduce_steps", nullable = true)
    public String getReproduceSteps() {
        return reproduceSteps;
    }

    public void setReproduceSteps(String reproduceSteps) {
        this.reproduceSteps = reproduceSteps;
    }

    @Basic
    @Column(name = "actual_result", nullable = true)
    public String getActualResult() {
        return actualResult;
    }

    public void setActualResult(String actualResult) {
        this.actualResult = actualResult;
    }

    @Basic
    @Column(name = "expected_result", nullable = true)
    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    @Basic
    @Column(name = "sprints", nullable = true)
    public String getSprints() {
        return sprints;
    }

    public void setSprints(String sprints) {
        this.sprints = sprints;
    }

    @Basic
    @Column(name = "logging", nullable = true)
    public String getLogging() {
        return logging;
    }

    public void setLogging(String logging) {
        this.logging = logging;
    }

    @Basic
    @Column(name = "comments", nullable = true)
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Basic
    @Column(name = "environments", nullable = true)
    public String getEnvironments() {
        return environments;
    }

    public void setEnvironments(String environments) {
        this.environments = environments;
    }

    @Basic
    @Column(name = "parent", nullable = true)
    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JrItem jrItem = (JrItem) o;

        if (rowId != jrItem.rowId) return false;
        if (itemKey != null ? !itemKey.equals(jrItem.itemKey) : jrItem.itemKey != null) return false;
        if (reporter != null ? !reporter.equals(jrItem.reporter) : jrItem.reporter != null) return false;
        if (assignee != null ? !assignee.equals(jrItem.assignee) : jrItem.assignee != null) return false;
        if (team != null ? !team.equals(jrItem.team) : jrItem.team != null) return false;
        if (type != null ? !type.equals(jrItem.type) : jrItem.type != null) return false;
        if (summary != null ? !summary.equals(jrItem.summary) : jrItem.summary != null) return false;
        if (description != null ? !description.equals(jrItem.description) : jrItem.description != null) return false;
        if (priority != null ? !priority.equals(jrItem.priority) : jrItem.priority != null) return false;
        if (status != null ? !status.equals(jrItem.status) : jrItem.status != null) return false;
        if (createdDate != null ? !createdDate.equals(jrItem.createdDate) : jrItem.createdDate != null) return false;
        if (updated != null ? !updated.equals(jrItem.updated) : jrItem.updated != null) return false;
        if (dueDate != null ? !dueDate.equals(jrItem.dueDate) : jrItem.dueDate != null) return false;
        if (labels != null ? !labels.equals(jrItem.labels) : jrItem.labels != null) return false;
        if (attachments != null ? !attachments.equals(jrItem.attachments) : jrItem.attachments != null) return false;
        if (testData != null ? !testData.equals(jrItem.testData) : jrItem.testData != null) return false;
        if (logging != null ? !logging.equals(jrItem.logging) : jrItem.logging != null) return false;
        if (expectedResult != null ? !expectedResult.equals(jrItem.expectedResult) : jrItem.expectedResult != null)
            return false;
        if (actualResult != null ? !actualResult.equals(jrItem.actualResult) : jrItem.actualResult != null)
            return false;
        if (reproduceSteps != null ? !reproduceSteps.equals(jrItem.reproduceSteps) : jrItem.reproduceSteps != null)
            return false;
        if (comments != null ? !comments.equals(jrItem.comments) : jrItem.comments != null) return false;
        if (sprints != null ? !sprints.equals(jrItem.sprints) : jrItem.sprints != null) return false;
        if (environments != null ? !environments.equals(jrItem.environments) : jrItem.environments != null)
            return false;
        if (parent != null ? !parent.equals(jrItem.parent) : jrItem.parent != null) return false;
        return isSubtask == jrItem.isSubtask;
    }

    @Override
    public int hashCode() {
        int result = rowId;
        result = 31 * result + (itemKey != null ? itemKey.hashCode() : 0);
        result = 31 * result + (reporter != null ? reporter.hashCode() : 0);
        result = 31 * result + (assignee != null ? assignee.hashCode() : 0);
        result = 31 * result + (team != null ? team.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (updated != null ? updated.hashCode() : 0);
        result = 31 * result + (dueDate != null ? dueDate.hashCode() : 0);
        result = 31 * result + (isSubtask ? 1 : 0);
        result = 31 * result + (labels != null ? labels.hashCode() : 0);
        result = 31 * result + (attachments != null ? attachments.hashCode() : 0);
        result = 31 * result + (testData != null ? testData.hashCode() : 0);
        result = 31 * result + (logging != null ? logging.hashCode() : 0);
        result = 31 * result + (actualResult != null ? actualResult.hashCode() : 0);
        result = 31 * result + (expectedResult != null ? expectedResult.hashCode() : 0);
        result = 31 * result + (reproduceSteps != null ? reproduceSteps.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + (sprints != null ? sprints.hashCode() : 0);
        result = 31 * result + (labels != null ? labels.hashCode() : 0);
        result = 31 * result + (environments != null ? environments.hashCode() : 0);
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }
}
