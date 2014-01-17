package org.deel.test;

import java.io.File;
import java.io.FileInputStream;

import org.deel.dao.FilePathDao;
import org.deel.dao.FolderDao;
import org.deel.domain.FilePath;
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
	private FilePathDao filePathDao;
	
	@Mock
	private FolderDao folderDao;
	

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
		
		fileService.saveNewFile(u, "random0", "/", file);
		
		
		verify(folderDao).loadFolderByPath(anyString(), any(User.class));
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
		
		fileService.saveNewFile(u, "random0", "/", file);
		
		
		File f = new File ("/home/garulf/info/esami/AE/code/storage/nick/random0");
		
		Assert.assertTrue(f.exists());
		
	}
	
}
