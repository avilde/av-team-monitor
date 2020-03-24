package server.jira.services;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import global.L;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

class JiraConnMgr {
    private static final String JIRA_FILE = "jira.properties";
    private JiraRestClientFactory factory;
    private JiraRestClient client;
    private InputStream jiraResourceFile;
    private Properties jiraProps;

    protected JiraRestClient getAsyncClient() throws IOException, URISyntaxException {
        jiraProps = new Properties();
        try {
            jiraResourceFile = getClass().getClassLoader().getResourceAsStream(JIRA_FILE);
            if (jiraResourceFile != null) {
                jiraProps.load(jiraResourceFile);
            } else {
                L.LOGGER.info("Credentials not found. File: " + JIRA_FILE);
            }

		} catch (Exception e) {
            L.LOGGER.info("Exception: " + e);
        } finally {
            if (jiraResourceFile != null)
                jiraResourceFile.close();
        }
        factory = new AsynchronousJiraRestClientFactory();
        client = factory.createWithBasicHttpAuthentication(new URI(jiraProps.getProperty("url")), System.getProperty("tm.user"), System.getProperty("tm.pass"));

        L.LOGGER.info("Retrieved async jira server connection.");

		return client;
	}
}
