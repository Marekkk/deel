package org.deel.dao.impl;

import java.util.List;

import org.deel.dao.UserDAO;
import org.deel.domain.User;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class UserDaoImpl implements UserDAO{


	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Long insertUser(User u) {
		return (Long) sessionFactory.getCurrentSession().save(u);
	}

	@Override
	public User findUserByUsername(String username) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("select u.id from " + User.class.getName() + " u where u.username = :usrname");
		query.setParameter("usrname", username);
		Long id = (Long) query.uniqueResult();
		if (id == null)
			return null;
		User user = (User) session.get(User.class, id);
		return user;
	}

	@Override
	public void updateUser(User u) {
		Session session = sessionFactory.getCurrentSession();
		session.update(u);
		return;
	}

	@Override
	public void deleteUser(User u) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(u);
	}

	@Override
	public User get(User u) {
		return (User)sessionFactory.getCurrentSession().get(User.class, u.getId());
	}

	@Override
	public List<User> userList() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(User.class);
		System.out.println(crit.list());
		return crit.list();
	}	
}
