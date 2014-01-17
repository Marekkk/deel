package org.deel.dao.impl;

import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.deel.dao.FilePathDao;
import org.deel.domain.FilePath;
import org.deel.domain.Folder;
import org.deel.domain.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class FilePathDaoImpl implements FilePathDao {
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	@Transactional
	public void insertFilePath(FilePath fp) {
		Session session = sessionFactory.getCurrentSession();
		session.save(fp);
		
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
		String q = "from " + FilePath.class.getName();
		Query query = sessionFactory.getCurrentSession().createQuery(q);
		//query.setParameter("name", "home");
		paths = query.list();	
		return paths;
	}

}
