package org.deel.dao.impl;

import org.deel.dao.FolderDao;
import org.deel.domain.Folder;
import org.deel.domain.User;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class FolderDaoImpl implements FolderDao {
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public void insertFolder(Folder f) {
		sessionFactory.getCurrentSession().save(f);
	}

	@Override
	public void deleteFolder(Folder f) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateFolder(Folder f) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Folder getFolder(String name, User user) {
		Long id = null;
		Query query = sessionFactory.getCurrentSession().createQuery("select f.id from Folder f where f.name = :name and f.user_id = :usr");
		query.setParameter("name", name);
		query.setParameter("usr", user.getId());
		id = (Long) query.uniqueResult();
		Folder f = (Folder) sessionFactory.getCurrentSession().get(Folder.class, id);
		return f;
	}

	@Override
	public Folder loadFolderByPath(String path, User user) {
		
		return null;
	}

	@Override
	public Folder get(Folder f) {
		// TODO Auto-generated method stub
		return null;
	}

}
