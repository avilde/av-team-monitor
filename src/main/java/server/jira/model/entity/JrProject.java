package server.jira.model.entity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "jr_project", schema = "team_monitor", catalog = "")
public class JrProject {
    private int rowId;
    private String projectName;
    private String projectKey;
    private Collection<JrProjTeam> jrProjTeamByRowId;
    private Boolean activeFlg;
    private Boolean sprintFlg;

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
    @Column(name = "project_key", nullable = false, length = 10)
    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    @Basic
    @Column(name = "active_flg", nullable = false)
    public Boolean getActiveFlg() {
        return activeFlg;
    }

    public void setActiveFlg(Boolean activeFlg) {
        this.activeFlg = activeFlg;
    }

    @Basic
    @Column(name = "sprint_flg", nullable = false)
    public Boolean getSprintFlg() {
        return sprintFlg;
    }

    public void setSprintFlg(Boolean sprintFlg) {
        this.sprintFlg = sprintFlg;
    }

    @OneToMany(mappedBy = "jrProjectByProjId")
    public Collection<JrProjTeam> getJrProjTeamByRowId() {
        return jrProjTeamByRowId;
    }

    public void setJrProjTeamByRowId(Collection<JrProjTeam> jrProjTeamByRowId) {
        this.jrProjTeamByRowId = jrProjTeamByRowId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JrProject that = (JrProject) o;

        if (rowId != that.rowId) return false;
        if (projectName != null ? !projectName.equals(that.projectName) : that.projectName != null) return false;
        if (projectKey != null ? !projectKey.equals(that.projectKey) : that.projectKey != null) return false;
        if (activeFlg != that.activeFlg) return false;
        return sprintFlg == that.sprintFlg;
    }

    @Override
    public int hashCode() {
        int result = rowId;
        result = 31 * result + (projectName != null ? projectName.hashCode() : 0);
        result = 31 * result + (projectKey != null ? projectKey.hashCode() : 0);
        result = 31 * result + (activeFlg ? 1 : 0);
        result = 31 * result + (sprintFlg ? 1 : 0);
        return result;
    }
}