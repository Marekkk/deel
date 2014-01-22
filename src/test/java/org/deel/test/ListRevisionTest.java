package org.deel.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.transaction.Transactional;

import junit.framework.Assert;

import org.deel.dao.UserDAO;
import org.deel.domain.File;
import org.deel.domain.FilePath;
import org.deel.domain.FileRevision;
import org.deel.domain.User;
import org.deel.service.FileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@WebAppConfiguration
@Transactional
public class ListRevisionTest {

	private FileService fileService;
	private UserDAO userDAO;
	
	
	public UserDAO getUserDAO() {
		return userDAO;
	}

	@Autowired
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public FileService getFileService() {
		return fileService;
	}

	@Autowired
	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

	/* db DEPENDENT test */
	@Test
	public void listRevision() throws Exception {
		User u = new User();
		u.setId((long)1);
		
		u = userDAO.get(u);
		
		FilePath fp = new FilePath();
		fp.setId((long)6);
		
		List<FileRevision> list = fileService.getRevisionList(u, fp);
		
		System.out.println(list);
		
		Assert.assertEquals(list.size(), 2);
		Assert.assertTrue(list.get(0).getId() > list.get(1).getId());
	}
	@Test
	public void getRevisionTest() throws IOException {
		User u = new User();
		u.setUsername("nick");
		u.setId((long)1);
		
		File f = new File();
		
		
		FilePath fp = new FilePath();
		fp.setId((long)6);
		fp.setFile(f);

		FileRevision fr = new FileRevision();
		fr.setId((long)4);
		
		java.io.FileOutputStream fOut = new FileOutputStream("test");
		OutputStreamWriter bw = new OutputStreamWriter(fOut);
		bw.write("test");
		bw.flush();
		bw.close();
		fOut.flush();
		fOut.close();
		
		java.io.FileInputStream fIn = new FileInputStream("test");
		
//		when(fileR)
		
		FileInputStream in = fileService.getRevision(u, fp, fr);
		BufferedReader r = new BufferedReader(new InputStreamReader(in));
		String line;
		while((line = r.readLine()) != null)
		{
			System.out.println(line);
		}
		
		
	}
}
