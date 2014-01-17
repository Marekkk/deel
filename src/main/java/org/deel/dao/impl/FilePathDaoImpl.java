package org.deel.dao.impl;

import org.deel.dao.FilePathDao;
import org.deel.domain.FilePath;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class FilePathDaoImpl implements FilePathDao {
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public void insertFilePath(FilePath fp) {
		Session session = sessionFactory.getCurrentSession();
		session.save(fp);
		
	}

	@Override
	public void deleteFilePath(FilePath fp) {
		sessionFactory.getCurrentSession().delete(fp);
	}

	@Override
	public void updateFilePath(FilePath fp) {
		sessionFactory.getCurrentSession().update(fp);
	}
}
