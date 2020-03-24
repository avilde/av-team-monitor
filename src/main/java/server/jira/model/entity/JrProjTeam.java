package server.jira.model.entity;

import server.jira.model.entity.keys.JrProjTeamPK;
import server.team.model.entity.TmTeam;

import javax.persistence.*;

@Entity
@Table(name = "jr_proj_team", schema = "team_monitor")
@IdClass(JrProjTeamPK.class)
@SuppressWarnings("ALL")
public class JrProjTeam {
    private int rowId;
    private int projId;
    private int teamId;
    private JrProject jrProjectByProjId;
    private TmTeam tmTeamByTeamId;

    @Basic
    @Column(name = "row_id", nullable = false)
    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    @Id
    @Column(name = "proj_id", nullable = false)
    public int getProjId() {
        return projId;
    }

    public void setProjId(int projId) {
        this.projId = projId;
    }

    @Id
    @Column(name = "team_id", nullable = false)
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

        JrProjTeam that = (JrProjTeam) o;

        if (rowId != that.rowId) return false;
        if (projId != that.projId) return false;
        if (teamId != that.teamId) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = rowId;
        result = 31 * result + projId;
        result = 31 * result + teamId;
        return result;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "proj_id", referencedColumnName = "row_id", nullable = false, insertable = false, updatable = false)
    public JrProject getJrProjectByProjId() {
        return jrProjectByProjId;
    }

    public void setJrProjectByProjId(JrProject jrProjectByProjId) {
        this.jrProjectByProjId = jrProjectByProjId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id", referencedColumnName = "row_id", nullable = false, insertable = false, updatable = false)
    public TmTeam getTmTeamByTeamId() {
        return tmTeamByTeamId;
    }

    public void setTmTeamByTeamId(TmTeam tmTeamByTeamId) {
        this.tmTeamByTeamId = tmTeamByTeamId;
    }
}
