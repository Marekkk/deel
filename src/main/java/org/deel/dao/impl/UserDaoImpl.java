package org.deel.dao.impl;

import org.deel.dao.UserDao;
import org.deel.domain.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDaoImpl implements UserDao{

	private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public void insertUser(User u) {
		sessionFactory.getCurrentSession().saveOrUpdate(u);
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}	
}
