package org.deel.dao.impl;

import org.deel.dao.TeamDAO;
import org.deel.domain.Team;
import org.deel.domain.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class TeamDaoImpl implements TeamDAO {
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void insertTeam(Team t) {
		sessionFactory.getCurrentSession().save(t);
	}

	@Override
	public void deleteTeam(Team t) {
		sessionFactory.getCurrentSession().delete(t);
	}

	@Override
	public void updateTeam(Team t) {
		sessionFactory.getCurrentSession().update(t);
	}

	@Override
	public Team findTeamByName(String name) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("select t.id from " + Team.class.getName() + " t where t.name = :tname");
		query.setParameter("tname", name);
		Long id = (Long) query.uniqueResult();
		if (id == null)
			return null;
		Team team = (Team) session.get(Team.class, id);
		return team;
	}

	@Override
	public Team get(Team t) {
		return (Team)sessionFactory.getCurrentSession().get(Team.class, t.getId());
		
	}

}
