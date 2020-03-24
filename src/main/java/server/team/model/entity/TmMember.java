package server.team.model.entity;

import javax.persistence.*;
import java.util.Arrays;

/**
 * Created by Andris on 25/12/2016.
 */
@Entity
@Table(name = "tm_member", schema = "team_monitor", catalog = "")
public class TmMember {
    private int rowId;
    private String login;
    private String role;
    private String nickName;
    private byte[] description;

    @Id
    @Column(name = "row_id", nullable = false)
    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    @Basic
    @Column(name = "login", nullable = false, length = 20)
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Basic
    @Column(name = "role", nullable = true, length = 100)
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Basic
    @Column(name = "nick_name", nullable = false, length = 20)
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Basic
    @Column(name = "description", nullable = true)
    public byte[] getDescription() {
        return description;
    }

    public void setDescription(byte[] description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TmMember tmMember = (TmMember) o;

        if (rowId != tmMember.rowId) return false;
        if (login != null ? !login.equals(tmMember.login) : tmMember.login != null) return false;
        if (role != null ? !role.equals(tmMember.role) : tmMember.role != null) return false;
        if (nickName != null ? !nickName.equals(tmMember.nickName) : tmMember.nickName != null) return false;
        if (!Arrays.equals(description, tmMember.description)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rowId;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (nickName != null ? nickName.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(description);
        return result;
    }
}
