package org.deel.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.management.RuntimeErrorException;

import org.deel.dao.FolderDao;
import org.deel.dao.UserDao;
import org.deel.domain.File;
import org.deel.domain.FilePath;
import org.deel.domain.Folder;
import org.deel.domain.User;
import org.deel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class UserServiceImpl implements UserService {

	private UserDao userDao;
	private FolderDao folderDao;
	private String storagePath = "/home/garulf/info/esami/AE/code/storage/";

	
	public FolderDao getFolderDao() {
		return folderDao;
	}
	
	@Autowired
	public void setFolderDao(FolderDao folderDao) {
		this.folderDao = folderDao;
	}
	
	public UserDao getUserDAO() {
		return userDao;
	}

	@Autowired
	public void setUserDAO(UserDao userDAO) {
		this.userDao = userDAO;
	}
	
	

	@Override
	@Transactional
	public void registerNewUser(User user) throws IOException  {
		if (userDao.findUserByUsername(user.getUsername()) != null)
			throw new RuntimeErrorException(new Error("username.exists"),
					"Username already exists!");
		
		
		userDao.insertUser(user);
		
		Folder f = new Folder();
		f.setFather(null);
		f.setName("/");
		f.setFSPath("/");
		f.setUser(user);
		/* Move FS related function in another class TODO */
		folderDao.insertFolder(f);
		
		//mkdir(f);
		
	}

	private void mkdir(Folder f) 
			throws IOException {
		
		java.io.File dir = new java.io.File(storagePath + f.getUser().getUsername()+f.getFSPath());
		if(!dir.mkdir())
			throw new RuntimeErrorException(new Error("directory.notcreated"), "Can't make dir" + dir.getAbsolutePath());
	}

	@Override
	@Transactional
	public User findUserByUsername(String username) {
		return userDao.findUserByUsername(username);

	}

	@Override
	public Set<Folder> getFolder(User user) {
		return user.getFolders();
	}

	@Override
	public Set<FilePath> getFilePaths(User user) {
		return user.getPaths();
	}

	@Override
	public Set<File> getFiles(User user) {
		return user.getFiles();
	}

	@Override
	public void updateUser(User user) {
		userDao.updateUser(user);
	}

	@Override
	public void addFolder(User user, Folder folder) {
		folder.setUser(user);
		folderDao.insertFolder(folder);
	}

}
