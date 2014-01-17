package org.deel.dao.impl;

import org.deel.dao.FileDao;
import org.deel.domain.File;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class FileDaoImpl implements FileDao {
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public void insertFile(File f) {
		sessionFactory.getCurrentSession().save(f);
	}

	@Override
	public void deleteFile(File f) {
		sessionFactory.getCurrentSession().delete(f);
	}

	@Override
	public void updateFile(File f) {
		sessionFactory.getCurrentSession().update(f);
	}

}
