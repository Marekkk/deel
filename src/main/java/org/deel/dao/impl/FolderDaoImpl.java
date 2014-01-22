package org.deel.dao.impl;

import org.deel.dao.FolderDAO;
import org.deel.domain.Folder;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class FolderDaoImpl implements FolderDAO {
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public void insertFolder(Folder f) {
		Long id = (Long)sessionFactory.getCurrentSession().save(f);
		f.setId(id);
	}

	@Override
	public void deleteFolder(Folder f) {
		sessionFactory.getCurrentSession().delete(f);
	}

	@Override
	public void updateFolder(Folder f) {
		sessionFactory.getCurrentSession().update(f);
	}

	@Override
	public Folder get(Folder f) {
		return (Folder) sessionFactory.getCurrentSession().get(Folder.class, f.getId());
	}

}
