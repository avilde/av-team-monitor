package server.jira.model.dao;

import global.L;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import server.jira.model.entity.JrItem;
import server.jira.model.entity.JrProjectUsers;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Transactional(noRollbackFor = Exception.class)
@Component("JiraDao")
public class JiraDaoImpl implements JiraDao {
	@Autowired
	public SessionFactory sessionFactory;

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public List<JrItem> getAllJiraItems() {
		CriteriaQuery<JrItem> q = getCurrentSession().getCriteriaBuilder().createQuery(JrItem.class);
		q.select(q.from(JrItem.class));

		return getCurrentSession().createQuery(q).getResultList();
	}

	@Override
	public void saveJiraItem(JrItem item) {
		getCurrentSession().save(item);
		getCurrentSession().flush();
		getCurrentSession().clear();
	}

	@Override
	public void deleteAllJiraItems() {
		CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
		CriteriaDelete<JrItem> q = cb.createCriteriaDelete(JrItem.class);
		q.from(JrItem.class);
		getCurrentSession().createQuery(q).executeUpdate();

		L.LOGGER.info("Truncated jira item table");
	}

	@Override
	public List<JrProjectUsers> getProjectUsers() throws IllegalArgumentException {
		TypedQuery<JrProjectUsers> q = getCurrentSession().createQuery("" +
				"select new server.jira.model.entity.JrProjectUsers(p.projectKey as project, p.sprintFlg as sprintFlg, group_concat(m.login) as users) " +
				"from JrProject p" +
				" left outer join JrProjTeam pt on pt.projId = p.rowId" +
				" left outer join TmTeamMember tm on pt.teamId = tm.teamId" +
				" left outer join TmMember m on tm.memberId = m.rowId " +
				"group by p.projectKey", JrProjectUsers.class);

		return q.getResultList();
	}


}
