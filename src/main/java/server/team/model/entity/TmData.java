package server.team.model.entity;

import server.team.model.dao.TeamsDao;

import java.io.Serializable;
import java.util.List;

public class TmData implements Serializable {
    public List<TmSetting> settings;
    public List<TmEnvironment> environments;
    public List<TmTeam> teams;

    public TmData(TeamsDao teamsDao) {
        this.settings = teamsDao.getClientSettings();
        this.environments = teamsDao.getEnvironments();
        this.teams = teamsDao.getAllTeamData();
    }
}
