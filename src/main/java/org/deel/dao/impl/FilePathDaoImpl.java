package org.deel.dao.impl;

import org.deel.dao.FilePathDAO;
import org.deel.domain.FilePath;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class FilePathDaoImpl implements FilePathDAO {
	
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

	@Override
	public FilePath getFilePath(FilePath fp) {
		return (FilePath) sessionFactory.getCurrentSession().get(FilePath.class, fp.getId());
	}
}
