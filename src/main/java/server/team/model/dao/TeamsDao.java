package server.team.model.dao;

import server.team.model.entity.TmEnvironment;
import server.team.model.entity.TmSetting;
import server.team.model.entity.TmTeam;
import server.team.model.entity.TmUserName;

import java.util.List;

/**
 * Created by jarmenz on 7/10/2017.
 */
public interface TeamsDao {
    List<TmUserName> getTeamUsers();

    List<TmTeam> getAllTeamData();

    List<TmEnvironment> getEnvironments();

    List<TmSetting> getServerSettings();

    List<TmSetting> getClientSettings();
}
