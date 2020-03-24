package server.team.controller;

import global.L;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import server.team.model.dao.TeamsDao;
import server.team.model.entity.TmData;

import javax.persistence.Cacheable;

@SuppressWarnings("unused")
@RestController
@Cacheable
@CrossOrigin(origins = "http://localhost:9000")
public class TeamsController {
    public TmData teamData;
    @Autowired
    @Qualifier("TeamsDao")
    private TeamsDao teamsDao;

    @RequestMapping(value = "getTeamMonitorData", method = RequestMethod.GET, headers = "Accept=application/json;charset=UTF-8")
    public @ResponseBody
    TmData getTeamMonitorData() {
        if (teamData == null) {
            teamData = new TmData(teamsDao);
        }

        L.LOGGER.info("Getting team monitor data.");

        return teamData;
    }
}