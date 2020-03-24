package server.team.model.dao;

import global.L;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import server.team.model.entity.TmEnvironment;
import server.team.model.entity.TmSetting;
import server.team.model.entity.TmTeam;
import server.team.model.entity.TmUserName;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Transactional
@Component("TeamsDao")
public class TeamsDaoImpl implements TeamsDao {
	private final static String SERVER = "server";
	private final static String CLIENT = "client";
	// SESSION
	@Autowired
	public SessionFactory sessionFactory;

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public List<TmUserName> getTeamUsers() throws IllegalArgumentException {
		TypedQuery<TmUserName> q = getCurrentSession().createQuery("" +
				"select new server.team.model.entity.TmUserName(t.teamName, group_concat(m.login) as userNames) " +
				"from TmTeam t" +
				" inner join TmTeamMember tm on tm.teamId = t.rowId" +
				" inner join TmMember m on tm.memberId = m.rowId " +
				"group by t.teamName", TmUserName.class);

		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TmTeam> getAllTeamData() {
		CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<TmTeam> q = getCurrentSession().getCriteriaBuilder().createQuery(TmTeam.class);
		Root<TmTeam> from = q.from(TmTeam.class);
		q.select(from);
		q.where(cb.equal(from.get("activeFlg"), 1));

		L.LOGGER.info("Getting team data");

		return getCurrentSession().createQuery(q).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TmEnvironment> getEnvironments() {
		CriteriaQuery<TmEnvironment> q = getCurrentSession().getCriteriaBuilder().createQuery(TmEnvironment.class);
		q.select(q.from(TmEnvironment.class));

		L.LOGGER.info("Getting environments");

		return getCurrentSession().createQuery(q).getResultList();
	}

	@Override
	public List<TmSetting> getServerSettings() {
		CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();

		CriteriaQuery<TmSetting> q = cb.createQuery(TmSetting.class);
		Root<TmSetting> c = q.from(TmSetting.class);
		q.select(c);
		q.where(cb.equal(c.get("type"), SERVER));

		L.LOGGER.info("Getting " + SERVER + " settings");

		return getCurrentSession().createQuery(q).getResultList();
	}

	@Override
	public List<TmSetting> getClientSettings() {
		CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();

		CriteriaQuery<TmSetting> q = cb.createQuery(TmSetting.class);
		Root<TmSetting> c = q.from(TmSetting.class);
		q.select(c);
		q.where(cb.equal(c.get("type"), CLIENT));

		L.LOGGER.info("Getting " + CLIENT + " settings");

		return getCurrentSession().createQuery(q).getResultList();
	}
}

