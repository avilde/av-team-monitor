package server.alm.model.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Andris on 25/12/2016.
 */
@Entity
@Table(name = "qc_defect", schema = "team_monitor", catalog = "")
public class QcDefect {
    private int rowId;
    private String itemKey;
    private String projectCd;
    private Timestamp updated;
    private Timestamp created;
    private String createdBy;
    private String team;
    private String assignee;
    private String environment;
    private String releaseNum;
    private String severity;
    private String priority;
    private String system;
    private byte blockingFlg;
    private byte reproducibleFlg;
    private byte happyPathFlg;
    private String country;
    private String jiraItemId;
    private String status;
    private String category;
    private String devComments;
    private String description;
    private String testArea;
    private String externalId;
    private String summary;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "row_id", nullable = false)
    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    @Basic
    @Column(name = "defect_num", nullable = false, length = 20)
    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    @Basic
    @Column(name = "project_cd", nullable = false, length = 21)
    public String getProjectCd() {
        return projectCd;
    }

    public void setProjectCd(String projectCd) {
        this.projectCd = projectCd;
    }

    @Basic
    @Column(name = "last_upd", nullable = false)
    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    @Basic
    @Column(name = "created", nullable = false)
    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @Basic
    @Column(name = "created_by", nullable = false, length = 20)
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Basic
    @Column(name = "assigned_team", nullable = false, length = 45)
    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    @Basic
    @Column(name = "assigned_user", nullable = false, length = 20)
    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    @Basic
    @Column(name = "environment", nullable = true, length = 100)
    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    @Basic
    @Column(name = "release_num", nullable = true, length = 45)
    public String getReleaseNum() {
        return releaseNum;
    }

    public void setReleaseNum(String releaseNum) {
        this.releaseNum = releaseNum;
    }

    @Basic
    @Column(name = "severity", nullable = false, length = 10)
    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    @Basic
    @Column(name = "priority", nullable = true, length = 10)
    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Basic
    @Column(name = "system", nullable = true, length = 200)
    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    @Basic
    @Column(name = "blocking_flg", nullable = false)
    public byte getBlockingFlg() {
        return blockingFlg;
    }

    public void setBlockingFlg(byte blockingFlg) {
        this.blockingFlg = blockingFlg;
    }

    @Basic
    @Column(name = "reproducible_flg", nullable = false)
    public byte getReproducibleFlg() {
        return reproducibleFlg;
    }

    public void setReproducibleFlg(byte reproducibleFlg) {
        this.reproducibleFlg = reproducibleFlg;
    }

    @Basic
    @Column(name = "happy_path_flg", nullable = false)
    public byte getHappyPathFlg() {
        return happyPathFlg;
    }

    public void setHappyPathFlg(byte happyPathFlg) {
        this.happyPathFlg = happyPathFlg;
    }

    @Basic
    @Column(name = "country", nullable = true, length = 45)
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Basic
    @Column(name = "jira_item_id", nullable = true, length = 45)
    public String getJiraItemId() {
        return jiraItemId;
    }

    public void setJiraItemId(String jiraItemId) {
        this.jiraItemId = jiraItemId;
    }

    @Basic
    @Column(name = "status_cd", nullable = true, length = 45)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "category", nullable = true, length = 25)
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Basic
    @Column(name = "dev_comments", nullable = true, length = -1)
    public String getDevComments() {
        return devComments;
    }

    public void setDevComments(String devComments) {
        this.devComments = devComments;
    }

    @Basic
    @Column(name = "description", nullable = true, length = -1)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "test_area", nullable = true, length = 100)
    public String getTestArea() {
        return testArea;
    }

    public void setTestArea(String testArea) {
        this.testArea = testArea;
    }

    @Basic
    @Column(name = "external_id", nullable = true, length = 100)
    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @Basic
    @Column(name = "title", nullable = false, length = 120)
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QcDefect qcDefect = (QcDefect) o;

        if (rowId != qcDefect.rowId) return false;
        if (blockingFlg != qcDefect.blockingFlg) return false;
        if (reproducibleFlg != qcDefect.reproducibleFlg) return false;
        if (happyPathFlg != qcDefect.happyPathFlg) return false;
        if (itemKey != null ? !itemKey.equals(qcDefect.itemKey) : qcDefect.itemKey != null) return false;
        if (projectCd != null ? !projectCd.equals(qcDefect.projectCd) : qcDefect.projectCd != null) return false;
        if (updated != null ? !updated.equals(qcDefect.updated) : qcDefect.updated != null) return false;
        if (created != null ? !created.equals(qcDefect.created) : qcDefect.created != null) return false;
        if (createdBy != null ? !createdBy.equals(qcDefect.createdBy) : qcDefect.createdBy != null) return false;
        if (team != null ? !team.equals(qcDefect.team) : qcDefect.team != null)
            return false;
        if (assignee != null ? !assignee.equals(qcDefect.assignee) : qcDefect.assignee != null)
            return false;
        if (environment != null ? !environment.equals(qcDefect.environment) : qcDefect.environment != null)
            return false;
        if (releaseNum != null ? !releaseNum.equals(qcDefect.releaseNum) : qcDefect.releaseNum != null) return false;
        if (severity != null ? !severity.equals(qcDefect.severity) : qcDefect.severity != null) return false;
        if (priority != null ? !priority.equals(qcDefect.priority) : qcDefect.priority != null) return false;
        if (system != null ? !system.equals(qcDefect.system) : qcDefect.system != null) return false;
        if (country != null ? !country.equals(qcDefect.country) : qcDefect.country != null) return false;
        if (jiraItemId != null ? !jiraItemId.equals(qcDefect.jiraItemId) : qcDefect.jiraItemId != null) return false;
        if (status != null ? !status.equals(qcDefect.status) : qcDefect.status != null) return false;
        if (category != null ? !category.equals(qcDefect.category) : qcDefect.category != null) return false;
        if (devComments != null ? !devComments.equals(qcDefect.devComments) : qcDefect.devComments != null)
            return false;
        if (description != null ? !description.equals(qcDefect.description) : qcDefect.description != null)
            return false;
        if (testArea != null ? !testArea.equals(qcDefect.testArea) : qcDefect.testArea != null) return false;
        if (externalId != null ? !externalId.equals(qcDefect.externalId) : qcDefect.externalId != null) return false;
        if (summary != null ? !summary.equals(qcDefect.summary) : qcDefect.summary != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rowId;
        result = 31 * result + (itemKey != null ? itemKey.hashCode() : 0);
        result = 31 * result + (projectCd != null ? projectCd.hashCode() : 0);
        result = 31 * result + (updated != null ? updated.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (team != null ? team.hashCode() : 0);
        result = 31 * result + (assignee != null ? assignee.hashCode() : 0);
        result = 31 * result + (environment != null ? environment.hashCode() : 0);
        result = 31 * result + (releaseNum != null ? releaseNum.hashCode() : 0);
        result = 31 * result + (severity != null ? severity.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (system != null ? system.hashCode() : 0);
        result = 31 * result + (int) blockingFlg;
        result = 31 * result + (int) reproducibleFlg;
        result = 31 * result + (int) happyPathFlg;
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (jiraItemId != null ? jiraItemId.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (devComments != null ? devComments.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (testArea != null ? testArea.hashCode() : 0);
        result = 31 * result + (externalId != null ? externalId.hashCode() : 0);
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        return result;
    }
}
