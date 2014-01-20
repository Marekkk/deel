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

import org.deel.dao.FileDAO;
import org.deel.dao.FilePathDAO;
import org.deel.domain.FilePath;
import org.deel.domain.Folder;
import org.deel.domain.User;
import org.deel.service.FileService;
import org.deel.service.utils.FSUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:fileUploadIntegrationTest.xml")
@WebAppConfiguration
@Transactional
public class dbDeleteFileTest {
	private FileService fileService;
	
	private FileDAO filedao;
	
	private FilePathDAO filePathDAO;
	
	public FilePathDAO getFilePathDAO() {
		return filePathDAO;
	}

	@Autowired
	public void setFilePathDAO(FilePathDAO filePathDAO) {
		this.filePathDAO = filePathDAO;
	}


	public FileDAO getFiledao() {
		return filedao;
	}


	@Autowired
	public void setFiledao(FileDAO filedao) {
		this.filedao = filedao;
	}


	public FileService getFileService() {
		return fileService;
	}


	@Autowired
	public void setFileService(FileService fileService) {
		
		this.fileService = fileService;
		
	}



	@Test
	public void deleteFileTest() {
		
		return;
	}
}
