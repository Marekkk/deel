package org.deel.dao.impl;

import org.deel.dao.UserDao;
import org.deel.domain.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


public class UserDaoImpl implements UserDao{


	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void insertUser(User u) {
		sessionFactory.getCurrentSession().saveOrUpdate(u);
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
}
