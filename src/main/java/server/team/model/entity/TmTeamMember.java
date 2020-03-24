package server.team.model.entity;

import server.team.model.entity.keys.TmTeamMemberPK;

import javax.persistence.*;

/**
 * Created by Andris on 25/12/2016.
 */
@Entity
@Table(name = "tm_team_member", schema = "team_monitor")
@IdClass(TmTeamMemberPK.class)
public class TmTeamMember {
    private int rowId;
    private int memberId;
    private int teamId;
    private TmMember tmMemberByMemberId;
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
    @Column(name = "member_id", nullable = false)
    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
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

        TmTeamMember that = (TmTeamMember) o;

        if (rowId != that.rowId) return false;
        if (memberId != that.memberId) return false;
        if (teamId != that.teamId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rowId;
        result = 31 * result + memberId;
        result = 31 * result + teamId;
        return result;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", referencedColumnName = "row_id", nullable = false, insertable = false, updatable = false)
    public TmMember getTmMemberByMemberId() {
        return tmMemberByMemberId;
    }

    public void setTmMemberByMemberId(TmMember tmMemberByMemberId) {
        this.tmMemberByMemberId = tmMemberByMemberId;
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
