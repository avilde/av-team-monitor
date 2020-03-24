package server.jira.model.entity;

import javax.persistence.Basic;
import javax.persistence.Column;

public class JrTeamProjKey {
    private String teamName;
    private String projKeys;

    @Basic
    @Column(name = "proj_keys", nullable = false, length = 1000)
    public String getProjKeys() {
        return projKeys;
    }

    public void setProjKeys(String projKeys) {
        this.projKeys = projKeys;
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

        JrTeamProjKey that = (JrTeamProjKey) o;

        if (teamName != null ? !teamName.equals(that.teamName) : that.teamName != null) return false;
        if (projKeys != null ? !projKeys.equals(that.projKeys) : that.projKeys != null) return false;

        return true;
    }
}
