package server.team.model.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Andris on 25/12/2016.
 */
@Entity
@Table(name = "tm_team", schema = "team_monitor", catalog = "")
public class TmTeam {
    private int rowId;
    private byte activeFlg;
    private byte ownPageFlg;
    private String teamName;
    private String caption;
    private String qtips;
    private String avatars;
    private String description;
    private List<TmMember> members;
    private byte[] favicon;
    private String primaryColor;
    private String secondaryColor;

    @Id
    @Column(name = "row_id", nullable = false)
    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }


    @Basic
    @Column(name = "active_flg", nullable = false)
    public byte getActiveFlg() {
        return activeFlg;
    }

    public void setActiveFlg(byte activeFlg) {
        this.activeFlg = activeFlg;
    }

    @Basic
    @Column(name = "own_page_flg", nullable = false)
    public byte getOwnPageFlg() {
        return ownPageFlg;
    }

    public void setOwnPageFlg(byte ownPageFlg) {
        this.ownPageFlg = ownPageFlg;
    }

    @Basic
    @Column(name = "team_name", nullable = false, length = 45)
    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Basic
    @Column(name = "caption", nullable = false, length = 45)
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Basic
    @Column(name = "qtips", nullable = true, length = -1)
    public String getQtips() {
        return qtips;
    }

    public void setQtips(String qtips) {
        this.qtips = qtips;
    }

    @Basic
    @Column(name = "avatars", nullable = true, length = 200)
    public String getAvatars() {
        return avatars;
    }

    public void setAvatars(String avatars) {
        this.avatars = avatars;
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
    @Column(name = "favicon", nullable = true)
    public byte[] getFavicon() {
        return favicon;
    }

    public void setFavicon(byte[] favicon) {
        this.favicon = favicon;
    }

    @Basic
    @Column(name = "primary_color", nullable = true, length = 10)
    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    @Basic
    @Column(name = "secondary_color", nullable = true, length = 10)
    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }


    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinTable(name = "tm_team_member",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    public List<TmMember> getMembers() {
        return members;
    }

    public void setMembers(List<TmMember> members) {
        this.members = members;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TmTeam tmTeam = (TmTeam) o;

        if (rowId != tmTeam.rowId) return false;
        if (activeFlg != tmTeam.activeFlg) return false;
        if (ownPageFlg != tmTeam.ownPageFlg) return false;
        if (teamName != null ? !teamName.equals(tmTeam.teamName) : tmTeam.teamName != null) return false;
        if (caption != null ? !caption.equals(tmTeam.caption) : tmTeam.caption != null) return false;
        if (qtips != null ? !qtips.equals(tmTeam.qtips) : tmTeam.qtips != null) return false;
        if (avatars != null ? !avatars.equals(tmTeam.avatars) : tmTeam.avatars != null) return false;
        if (description != null ? !description.equals(tmTeam.description) : tmTeam.description != null) return false;
        if (!Arrays.equals(favicon, tmTeam.favicon)) return false;
        if (primaryColor != null ? !primaryColor.equals(tmTeam.primaryColor) : tmTeam.primaryColor != null)
            return false;
        if (secondaryColor != null ? !secondaryColor.equals(tmTeam.secondaryColor) : tmTeam.secondaryColor != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rowId;
        result = 31 * result + (int) activeFlg;
        result = 31 * result + (int) ownPageFlg;
        result = 31 * result + (teamName != null ? teamName.hashCode() : 0);
        result = 31 * result + (caption != null ? caption.hashCode() : 0);
        result = 31 * result + (qtips != null ? qtips.hashCode() : 0);
        result = 31 * result + (avatars != null ? avatars.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(favicon);
        result = 31 * result + (primaryColor != null ? primaryColor.hashCode() : 0);
        result = 31 * result + (secondaryColor != null ? secondaryColor.hashCode() : 0);

        return result;
    }
}
