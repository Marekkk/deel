package org.deel.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.deel.domain.File;
import org.deel.domain.FilePath;
import org.deel.domain.Folder;
import org.deel.domain.Team;
import org.deel.domain.User;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {
	@Transactional
	public void registerNewUser(User user) throws IOException;
	
	@Transactional
	public List<User> listUser(User curr);
	
	@Transactional
	public Set<Folder> getFolder(User user);
	
	@Transactional
	public Set<FilePath> getFilePaths(User user);
	
	@Transactional
	public Set<File> getFiles(User user);

	@Transactional
	public User findUserByUsername(String username);
	
	@Transactional
	public void updateUser(User user);
	
	@Transactional
	public void addFolder(User user, Folder folder);
	
	@Transactional
	public void addTeam(Team t);
	
	@Transactional
	public Team findTeamByName(String name);
	
	@Transactional
	public List<Team> getTeams();
}
