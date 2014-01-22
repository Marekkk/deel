package org.deel.test;


import java.util.Set;

import org.deel.dao.FileDAO;
import org.deel.dao.FilePathDAO;
import org.deel.dao.FolderDAO;
import org.deel.dao.UserDAO;
import org.deel.domain.Category;
import org.deel.domain.Category.type;
import org.deel.domain.File;
import org.deel.domain.FilePath;
import org.deel.domain.Folder;
import org.deel.domain.User;
import org.deel.service.FileService;
import org.deel.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

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
	
	@Test
	@Rollback(false)
	public void test1() {
		System.out.println("Test1...");
		User u = userDAO.findUserByUsername("rpiero");
		if (u == null) {
			u = new User("piero@gmail.com", "Piero", "Rametta", "rpiero", "abcd", new Category(type.Free, 20));
			userDAO.insertUser(u);
		}
		System.out.println(u);
	}
	
	@Test
	@Rollback(false)
	public void test2() {
		System.out.println("Test2...");
		User u = userDAO.findUserByUsername("rpiero");
		File f = new File("ciccio.txt", u);
		Folder fo = new Folder("abcd", null, u);
		FilePath fp = new FilePath("ciccio.txt", u, f, fo);
		filePathDAO.insertFilePath(fp);
	}
	
	@Test
	@Rollback(false)
	public void test3() {
		User u = userDAO.findUserByUsername("rpiero");
		System.out.println(u.getFiles());
		System.out.println(u.getPaths());
		System.out.println(u.getFolders());
	}
	
	@Test
	@Rollback(false)
	public void test4() {
		User u = userDAO.findUserByUsername("rpiero");
		Set<FilePath> paths = u.getPaths();
		System.out.println(paths);
		FilePath fp1 = new FilePath();
		fp1.setId(new Long(3));
		FilePath fp2 = filePathDAO.getFilePath(fp1);
		System.out.println(fp2);
		filePathDAO.deleteFilePath(fp2);
	}
	
	@Test
	@Rollback(true)
	public void test5() {
		User u = userDAO.findUserByUsername("rpiero");
	}
}

