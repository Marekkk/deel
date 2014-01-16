package org.deel.dao.impl;

import java.util.LinkedList;
import java.util.List;

import org.deel.dao.FilePathDao;
import org.deel.domain.FilePath;
import org.deel.domain.Folder;
import org.deel.domain.User;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class FilePathDaoImpl implements FilePathDao {
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public void insertFilePath(FilePath fp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteFilePath(FilePath fp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateFilePath(FilePath fp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<FilePath> listOfPathFiles(User user, Folder folder) {
		List<FilePath> paths = new LinkedList<FilePath>();
		Query query = sessionFactory.getCurrentSession().createQuery("from filepath fp where fp.user_id = :usr and fp.folder_id = :f");
		query.setParameter("usr", user.getId());
		query.setParameter("f", folder.getId());
		paths = query.list();
		return paths;
	}

}
