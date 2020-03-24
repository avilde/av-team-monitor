package server.team.model.entity.keys;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Andris on 25/12/2016.
 */
public class TmTeamMemberPK implements Serializable {
	private int memberId;
	private int teamId;

	@Column(name = "member_id", nullable = false)
	@Id
	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
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

		TmTeamMemberPK that = (TmTeamMemberPK) o;

		if (memberId != that.memberId) return false;
		if (teamId != that.teamId) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = memberId;
		result = 31 * result + teamId;
		return result;
	}
}
