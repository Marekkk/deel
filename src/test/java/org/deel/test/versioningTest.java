package org.deel.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.transaction.Transactional;

import org.apache.commons.io.output.WriterOutputStream;
import org.deel.dao.FileDAO;
import org.deel.dao.FilePathDAO;
import org.deel.dao.FolderDAO;
import org.deel.dao.UserDAO;
import org.deel.domain.File;
import org.deel.domain.FilePath;
import org.deel.domain.FileRevision;
import org.deel.domain.User;
import org.deel.service.FileService;
import org.deel.service.impl.FileServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


public class versioningTest {
	
	@Mock 
	private FileDAO fileDao;
	
	@Mock 
	private UserDAO userDao;
	
	@Mock
	private FilePathDAO filePathDao;
	
	@Mock 
	private FolderDAO folderDao;
	
//	@Mock
//	private FileRevisionDAO filerevisionDAO;
	
	@InjectMocks
	private FileService fileService;
	
	@Before
	public void setup() throws Exception {
		fileService = new FileServiceImpl();
		MockitoAnnotations.initMocks(this);
	}

	public FileService getFileService() {
		return fileService;
	}

	
	@Test
	public void newFileTest() throws IOException {
		User u = new User();
		u.setUsername("nick");
		u.setId((long)1);
		
		File f = new File();
		
		
		FilePath fp = new FilePath();
		fp.setId((long)2);
		fp.setFile(f);
		
		java.io.FileOutputStream fOut = new FileOutputStream("test");
		OutputStreamWriter bw = new OutputStreamWriter(fOut);
		bw.write("test");
		bw.flush();
		bw.close();
		fOut.flush();
		fOut.close();
		
		java.io.FileInputStream fIn = new FileInputStream("test");
		
//		when(fileR)
		
		fileService.updateFile(u, fp, fIn,(long)1000);
	}
	

	
}
