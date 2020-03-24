package server.jira.model.entity;

import javax.persistence.Basic;
import javax.persistence.Column;

public class JrProjectUsers {
    private String project;
    private String users;
    private Boolean sprintFlg;

    public JrProjectUsers(String project, Boolean sprintFlg, String users) {
        this.setProject(project);
        this.setSprintFlg(sprintFlg);
        this.setUsers(users);
    }

    @Basic
    @Column(name = "project", nullable = false)
    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    @Basic
    @Column(name = "users", nullable = false, length = 45)
    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    @Basic
    @Column(name = "sprint_flg", nullable = false)
    public Boolean getSprintFlg() {
        return sprintFlg;
    }

    public void setSprintFlg(Boolean sprintFlg) {
        this.sprintFlg = sprintFlg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JrProjectUsers that = (JrProjectUsers) o;

        if (users != null ? !users.equals(that.users) : that.users != null) return false;
        if (project != null ? project.equals(that.project) : that.project == null) return false;
        return sprintFlg == that.sprintFlg;
    }

}
