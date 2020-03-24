package server.alm.model.dao;

import global.L;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import server.alm.model.entity.QcDefect;
import server.alm.model.entity.QcProjTeam;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Transactional
@Component("DefectsDao")
public class DefectsDaoImpl implements DefectsDao {
	// SESSION
	@Autowired
	public SessionFactory sessionFactory;

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	// DEFECTS
	@Override
	public void saveDefect(QcDefect defect) {
		getCurrentSession().save(defect);
		getCurrentSession().flush();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getDefectDbId(String project, String defectNum) {
		int defectId = 0;
		Session session = getCurrentSession();

		if (!project.isEmpty() && !defectNum.isEmpty()) {
			try {
				Criteria criteria = session.createCriteria(QcDefect.class)
						.setProjection(Projections.projectionList()
								.add(Projections.property("rowId"), "rowId"))
						.add(Restrictions.eq("projectCd", project))
						.add(Restrictions.eq("defectNum", defectNum))
						.setResultTransformer(Transformers.aliasToBean(QcDefect.class));

				List<QcDefect> defects = criteria.list();

				for (QcDefect defect : defects) {
					if (defects.size() == 1) {
						defectId = defect.getRowId();
					}
				}
			} catch (Exception e) {
                L.LOGGER.error(e.getMessage());
            }
		}

		return defectId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<QcDefect> getDefects(String sRetrieveType, String sTeamType, String sSortBy) {
		Criteria criteria = getCurrentSession().createCriteria(QcDefect.class);

		switch (sRetrieveType) {
			case "all":
				break;
			case "active":
				criteria.add(Restrictions.not(Restrictions.eq("statusCd", "Postponed")));
				break;
			case "postponed":
				criteria.add(Restrictions.eq("statusCd", "Postponed"));
				break;
			case "fixed":
				criteria.add(Restrictions.eq("statusCd", "Fixed"));
				break;
		}

		switch (sTeamType) {
			case "all":
				break;
			default:
				criteria.add(Restrictions.eq("assignedTeam", sTeamType));
				break;
		}

		switch (sSortBy) {
			case "default":
			case "severity":
				criteria.addOrder(Order.asc("severity"));
				criteria.addOrder(Order.asc("projectCd"));
				criteria.addOrder(Order.asc("defectNum"));
				break;
			case "status":
				criteria.addOrder(Order.asc("statusCd"));
				criteria.addOrder(Order.asc("projectCd"));
				criteria.addOrder(Order.asc("defectNum"));
				break;
			case "priority":
				criteria.addOrder(Order.asc("priority"));
				criteria.addOrder(Order.asc("projectCd"));
				criteria.addOrder(Order.asc("defectNum"));
				break;
		}

        L.LOGGER.info("Getting " + sRetrieveType + " defects.");

		return (List<QcDefect>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<QcDefect> getAllDefects() {
		CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
		CriteriaQuery query = builder.createQuery(QcDefect.class);
		Root<QcDefect> root = query.from(QcDefect.class);
		query.select(root);

		return getCurrentSession().createQuery(query).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getTeamProjects() {
		Criteria criteria = getCurrentSession().createCriteria(QcProjTeam.class, "team_proj")
				.createAlias("team_proj.tmTeamByTeamId", "team")
				.createAlias("team_proj.qcProjectByProjId", "proj")
				.setProjection(Projections.projectionList()
						.add(Projections.property("team_proj.teamName"), "teamAlias")
						.add(Projections.property("proj.projectName"), "projectName")
						.add(Projections.property("proj.shortName"), "shortName")
						.add(Projections.property("proj.teamColumn"), "teamColumn")
						.add(Projections.property("team.teamName"), "teamName")
				)
				.add(Restrictions.eq("team.activeFlg", (byte) 1));

        L.LOGGER.info("Getting team projects.");

		return criteria.list();
	}

	@SuppressWarnings("uncheked")
	@Override
	public int truncateTable(String table) {
		int affectedRows;

		String sql = String.format("truncate table %s", table);

		Query query = getCurrentSession().createSQLQuery(sql);

		affectedRows = query.executeUpdate();

		getCurrentSession().flush();

		return affectedRows;
	}
}
