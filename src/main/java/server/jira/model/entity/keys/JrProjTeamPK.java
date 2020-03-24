package server.jira.model.entity.keys;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class JrProjTeamPK implements Serializable {
    private int projId;
    private int teamId;

    @Column(name = "proj_id", nullable = false)
    @Id
    public int getProjId() {
        return projId;
    }

    public void setProjId(int projId) {
        this.projId = projId;
    }

    @Column(name = "team_id", nullable = false)
    @Id
    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JrProjTeamPK that = (JrProjTeamPK) o;

        if (projId != that.projId) return false;
        if (teamId != that.teamId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = projId;
        result = 31 * result + teamId;
        return result;
    }
}
