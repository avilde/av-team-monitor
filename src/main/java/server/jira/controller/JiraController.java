package server.jira.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import server.jira.model.dao.JiraDao;
import server.jira.model.entity.JrItem;

import java.util.List;

@SuppressWarnings("unused")
@RestController
@CrossOrigin(origins = "http://localhost:9000")
public class JiraController {
    @Autowired
    @Qualifier("JiraDao")
    private JiraDao jiraDao;

    private List<JrItem> jrItems;

    @RequestMapping(value = "getJiraItems", method = RequestMethod.GET, headers = "Accept=application/json")
    public @ResponseBody
    List<JrItem> getJiraItems() {
        try {
            jrItems = jiraDao.getAllJiraItems();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jrItems;
    }

    @RequestMapping(value = "getAllJiraItems", method = RequestMethod.GET, headers = "Accept=application/json")
    public @ResponseBody
    List<JrItem> getAllJiraItems() {
        try {
            jrItems = jiraDao.getAllJiraItems();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jrItems;
    }
}
