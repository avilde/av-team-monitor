package server.alm.model.entity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Andris on 25/12/2016.
 */
@Entity
@Table(name = "qc_project", schema = "team_monitor", catalog = "")
public class QcProject {
    private int rowId;
    private String projectName;
    private String shortName;
    private String teamColumn;
    private Collection<QcProjTeam> qcProjTeamByRowId;

    @Id
    @Column(name = "row_id", nullable = false)
    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    @Basic
    @Column(name = "project_name", nullable = false, length = 100)
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Basic
    @Column(name = "short_name", nullable = false, length = 10)
    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Basic
    @Column(name = "team_column", nullable = false, length = 100)
    public String getTeamColumn() {
        return teamColumn;
    }

    public void setTeamColumn(String teamColumn) {
        this.teamColumn = teamColumn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QcProject that = (QcProject) o;

        if (rowId != that.rowId) return false;
        if (projectName != null ? !projectName.equals(that.projectName) : that.projectName != null) return false;
        if (shortName != null ? !shortName.equals(that.shortName) : that.shortName != null) return false;
        if (teamColumn != null ? !teamColumn.equals(that.teamColumn) : that.teamColumn != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rowId;
        result = 31 * result + (projectName != null ? projectName.hashCode() : 0);
        result = 31 * result + (shortName != null ? shortName.hashCode() : 0);
        result = 31 * result + (teamColumn != null ? teamColumn.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "qcProjectByProjId")
    public Collection<QcProjTeam> getQcProjTeamByRowId() {
        return qcProjTeamByRowId;
    }

    public void setQcProjTeamByRowId(Collection<QcProjTeam> qcProjTeamByRowId) {
        this.qcProjTeamByRowId = qcProjTeamByRowId;
    }
}
