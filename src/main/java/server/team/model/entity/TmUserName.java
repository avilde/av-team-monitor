package server.team.model.entity;

import javax.persistence.Basic;
import javax.persistence.Column;

public class TmUserName {
    private String teamName;
    private String userNames;

    public TmUserName(String teamName, String userNames) {
        this.setTeamName(teamName);
        this.setUserNames(userNames);
    }

    @Basic
    @Column(name = "user_names", nullable = false, length = 1000)
    public String getUserNames() {
        return userNames;
    }

    public void setUserNames(String userNames) {
        this.userNames = userNames;
    }

    @Basic
    @Column(name = "team_name", nullable = false, length = 45)
    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TmUserName that = (TmUserName) o;

        if (teamName != null ? !teamName.equals(that.teamName) : that.teamName != null) return false;
        if (userNames != null ? !userNames.equals(that.userNames) : that.userNames != null) return false;

        return true;
    }
}
