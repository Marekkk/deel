package org.deel.dao.impl;

import org.deel.dao.FileRevisionDAO;
import org.deel.domain.FilePath;
import org.deel.domain.FileRevision;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class FileRevisionDAOImpl implements FileRevisionDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public Long insert(FileRevision fr) {
		Session session = sessionFactory.getCurrentSession();
		Long id =  (Long) session.save(fr);
		fr.setId(id);
		return id;
		
		
	}

	@Override
	public void delete(FileRevision fr) {
		sessionFactory.getCurrentSession().delete(fr);
	}

	@Override
	public void update(FileRevision fr) {
		sessionFactory.getCurrentSession().update(fr);
		
	}

	@Override
	public FileRevision get(FileRevision fr) {
		return (FileRevision) sessionFactory.getCurrentSession().get(FilePath.class, fr.getId());
	}

}
