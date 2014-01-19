package org.deel.test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.deel.domain.FilePath;
import org.deel.domain.Folder;
import org.deel.domain.User;
import org.deel.service.FileService;
import org.deel.service.utils.FSUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:fileUploadIntegrationTest.xml")
@WebAppConfiguration
@Transactional
public class dbDeleteFileTest {
	private FileService fileService;
	
	
	
	public FileService getFileService() {
		return fileService;
	}


	@Autowired
	public void setFileService(FileService fileService) {
		
		this.fileService = fileService;
		
	}



	@Test
	public void deleteFileTest() {
		
		org.deel.domain.File dbFile = new org.deel.domain.File();
		dbFile.setFsPath("/deleteTest");
		User u = new User();
		u.setUsername("nick");
		u.setId((long)1);

		FilePath fp = new FilePath();
		fp.setId((long)5);
		fileService.deleteFile(u,  fp);
		

	}
}
