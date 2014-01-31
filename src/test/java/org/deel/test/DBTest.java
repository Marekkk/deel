package org.deel.test;

import java.util.LinkedList;
import java.util.List;

import org.deel.dao.FileDAO;
import org.deel.dao.FilePathDAO;
import org.deel.dao.FolderDAO;
import org.deel.dao.UserDAO;
import org.deel.domain.File;
import org.deel.domain.Folder;
import org.deel.domain.Team;
import org.deel.domain.User;
import org.deel.service.FileService;
import org.deel.service.UserService;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
@Transactional
public class DBTest {

	@Autowired
	UserService userService;

	@Autowired
	FileService fileService;
	
	@Autowired
	FolderDAO folderDAO;
	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	FileDAO fileDAO;
	
	@Autowired
	FilePathDAO filePathDAO;
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Before
	@Transactional
	public void init() {
		User u1 = new User("pierpaolo.rametta@gmail.com", "Piero", "Rametta", "rpiero", "abcd", null);
		User u2 = new User("francesco@gmail.com", "Francesco", "Rametta", "frengo", "abcd", null);
		userDAO.insertUser(u1);
		userDAO.insertUser(u2);
		Folder f = new Folder();
		f.setFather(null);
		f.setName("photos");
		f.setUser(u1);
		folderDAO.insertFolder(f);
		File file = new File();
		file.setName("ciccio.txt");
		file.setOwner(u1);
		fileDAO.insertFile(file);
	}
	
	@Test
	@Rollback(true)
	public void test1() {
		System.out.println("Test1...");
		User u = userService.findUserByUsername("rpiero");
		assertTrue(userService.listUser(u).size() == 1);
	}
	
	@Test
	@Rollback(true)
	public void test2() {
		User u = userService.findUserByUsername("dasdada");
		assertNull(u);
	}
	
	@Test
	@Rollback(true)
	public void test3() {
		User u1 = userService.findUserByUsername("rpiero");
		User u2 = userService.findUserByUsername("frengo");
		assertNotSame(u1, u2);
	}
	
	@Test
	@Rollback(true)
	public void test4() {
		User u1 = userService.findUserByUsername("rpiero");
		User u2 = userService.findUserByUsername("frengo");
		List<User> users = new LinkedList<User>();
		users.add(u2);
		Team t = new Team();
		t.setName("Ramettas");
		t.setCreatedBy(u1.getId());
		t.setUsersInTeam(users);
		userService.addTeam(t);
		assertFalse(userService.getTeams().isEmpty());
	}
}

