package server.jira.model.dao;

import server.jira.model.entity.JrItem;
import server.jira.model.entity.JrProjectUsers;

import java.util.List;

public interface JiraDao {
    List<JrItem> getAllJiraItems();

    void saveJiraItem(JrItem item);

    void deleteAllJiraItems();

    List<JrProjectUsers> getProjectUsers();
}
