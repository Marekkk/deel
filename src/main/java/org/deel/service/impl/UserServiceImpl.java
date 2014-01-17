package org.deel.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.management.RuntimeErrorException;

import org.deel.dao.FolderDAO;
import org.deel.dao.UserDAO;
import org.deel.domain.File;
import org.deel.domain.FilePath;
import org.deel.domain.Folder;
import org.deel.domain.User;
import org.deel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class UserServiceImpl implements UserService {


	private UserDAO userDAO;
	private FolderDAO folderDAO;
	private String storagePath = System.getProperty("user.home") + "/storage/";

	
	public FolderDAO getFolderDao() {
		return folderDAO;
	}
	
	@Autowired
	public void setFolderDao(FolderDAO folderDao) {
		System.out.println("setFoldeDao this: " );
		System.out.println(this);
		this.folderDAO = folderDao;
	}
	
	public UserDAO getUserDAO() {
		return userDAO;
	}

	@Autowired
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	@Override
	@Transactional
	public void registerNewUser(User user)   {
		if (userDAO.findUserByUsername(user.getUsername()) != null)
			throw new RuntimeErrorException(new Error("username.exists"),
					"Username already exists!");
		
		
		Long id = userDAO.insertUser(user);
		System.out.println("New User register with id -> " + user.getId());
		
		Folder f = new Folder();
		f.setFather(null);
		f.setName("/");
		f.setFsPath("/");
		f.setUser(user);
		user.getFolders().add(f);
		userDAO.insertUser(user);
		//userDAO.updateUser(user);
		/* Move FS related function in another class TODO */
		//folderDAO.insertFolder(f);
		/* Move Fs related function in another class TODO */
		//mkdir(f);
		
	}

	private void mkdir(Folder f) 
			throws IOException {
		
		java.io.File dir = new java.io.File(storagePath + f.getUser().getUsername()+f.getFsPath());
		if(!dir.mkdir())
			throw new RuntimeErrorException(new Error("directory.notcreated"), "Can't make dir" + dir.getAbsolutePath());
	}

	@Override
	@Transactional
	public User findUserByUsername(String username) {
		return userDAO.findUserByUsername(username);

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
		userDAO.updateUser(user);
	}

	@Override
	public void addFolder(User user, Folder folder) {
		folder.setUser(user);
		folderDAO.insertFolder(folder);
	}

}
