package org.deel.service.impl;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.management.RuntimeErrorException;
import javax.persistence.Transient;

import org.deel.dao.FolderDAO;
import org.deel.dao.TeamDAO;
import org.deel.dao.UserDAO;
import org.deel.domain.File;
import org.deel.domain.FilePath;
import org.deel.domain.Folder;
import org.deel.domain.Team;
import org.deel.domain.User;
import org.deel.service.UserService;
import org.deel.service.utils.FSUtils;
import org.deel.service.utils.FileSystemGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class UserServiceImpl implements UserService {


	private UserDAO userDAO;
	private FolderDAO folderDAO;
	private TeamDAO teamDAO;
	private FileSystemGateway fileSystemMapper;

	public FileSystemGateway getFileSystemGateway() {
		return fileSystemMapper;
	}

	@Autowired
	public void setFileSystemMapper(FileSystemGateway fileSystemMapper) {
		this.fileSystemMapper = fileSystemMapper;
	}
	public TeamDAO getTeamDao() {
		return teamDAO;
	}
	
	@Autowired
	public void setTeamDao(TeamDAO teamDao) {
		this.teamDAO = teamDao;
	}
	
	public FolderDAO getFolderDao() {
		return folderDAO;
	}
	
	@Autowired
	public void setFolderDao(FolderDAO folderDao) {
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
	public void registerNewUser(User user) throws IOException {
		if (userDAO.findUserByUsername(user.getUsername()) != null)
			throw new RuntimeErrorException(new Error("username.exists"),
					"Username already exists!");
		
		userDAO.insertUser(user);
		
		
		Folder f = new Folder();
		f.setFather(null);
		f.setName("/");
		f.setFsPath("/");
		f.setUser(user);
		
		//user.getFolders().add(f);
		//userDAO.updateUser(user);
		
		folderDAO.insertFolder(f);
		fileSystemMapper.mkdir(f.getCompleteFSPath());
				
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

	@Override
	@Transactional
	public List<User> listUser(User curr) {
		List <User> userList = userDAO.userList();
		for (Iterator<User> it = userList.listIterator();it.hasNext();) {
			if (it.next().getId() == curr.getId())
				it.remove();
		}
		return userList;
	}

	@Override
	public void addTeam(Team t) {
		teamDAO.insertTeam(t);
	}

	@Override
	public Team findTeamByName(String name) {
		return teamDAO.findTeamByName(name);
	}

	@Override
	@Transactional
	public List<Team> getTeams() {
		return teamDAO.getTeams();
	}
}
