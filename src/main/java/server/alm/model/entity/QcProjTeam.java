package server.alm.model.entity;

import server.alm.model.entity.keys.QcProjTeamPK;
import server.team.model.entity.TmTeam;

import javax.persistence.*;

/**
 * Created by Andris on 25/12/2016.
 */
@Entity
@Table(name = "qc_proj_team", schema = "team_monitor")
@IdClass(QcProjTeamPK.class)
public class QcProjTeam {
    private int rowId;
    private int projId;
    private int teamId;
    private String teamName;
    private QcProject qcProjectByProjId;
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

    @Basic
    @Column(name = "team_name", nullable = false, length = 100)
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

        QcProjTeam that = (QcProjTeam) o;

        if (rowId != that.rowId) return false;
        if (projId != that.projId) return false;
        if (teamId != that.teamId) return false;
        if (teamName != null ? !teamName.equals(that.teamName) : that.teamName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rowId;
        result = 31 * result + projId;
        result = 31 * result + teamId;
        result = 31 * result + (teamName != null ? teamName.hashCode() : 0);
        return result;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "proj_id", referencedColumnName = "row_id", nullable = false, insertable = false, updatable = false)
    public QcProject getQcProjectByProjId() {
        return qcProjectByProjId;
    }

    public void setQcProjectByProjId(QcProject qcProjectByProjId) {
        this.qcProjectByProjId = qcProjectByProjId;
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
