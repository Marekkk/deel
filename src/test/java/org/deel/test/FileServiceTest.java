package org.deel.test;

import java.io.File;
import java.io.FileInputStream;

import javax.transaction.Transactional;

import org.deel.dao.FileDAO;
import org.deel.dao.FilePathDAO;
import org.deel.dao.FolderDAO;
import org.deel.domain.FilePath;
import org.deel.domain.Folder;
import org.deel.domain.User;
import org.deel.service.FileService;
import org.deel.service.impl.FileServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Matchers.*;


public class FileServiceTest {
	
	@Mock
	private FilePathDAO filePathDao;
	
	@Mock
	private FolderDAO folderDao;
	
	@Mock
	private FileDAO fileDao;
	

	@InjectMocks
	private FileService fileService;
	
	@Before
	public void setup() {
		fileService = new FileServiceImpl();
		MockitoAnnotations.initMocks(this);
	}
	
	
	@Test
	public void dbInteractionTest() throws Exception {
		FileInputStream file = new FileInputStream("/home/garulf/info/esami/AE/code/random0");
		
		User u = new User();
		u.setUsername("nick");
		
		Folder folder = new Folder();
		folder.setId((long) 1);
		folder.setFsPath("/");
		
		when(folderDao.get(any(Folder.class))).thenReturn(folder);
		
		fileService.uploadFile(u, "random0", folder, file);
		

		verify(filePathDao).insertFilePath(any(FilePath.class));
		
		File f = new File ("/home/garulf/info/esami/AE/code/storage/nick/random0");
		Assert.assertTrue(f.exists());
		f.delete();
	}
	
	@Test
	public void saveOnFilesystemTest() throws Exception {
		FileInputStream file = new FileInputStream("/home/garulf/info/esami/AE/code/random0");
		
		User u = new User();
		u.setUsername("nick");
		Folder folder = new Folder();
		folder.setId((long) 1);
		
		fileService.uploadFile(u, "random0", folder, file);
		
		
		File f = new File ("/home/garulf/info/esami/AE/code/storage/nick/random0");
		
		Assert.assertTrue(f.exists());
		
	}
	
}
