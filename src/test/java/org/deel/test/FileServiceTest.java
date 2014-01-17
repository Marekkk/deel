package org.deel.test;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;

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
	public void dbInteractionUploadTest() throws Exception {
		FileInputStream file = new FileInputStream("/home/garulf/info/esami/AE/code/random0");
		
		User u = new User();
		u.setUsername("nick");
		u.setId((long)1);
		
		Folder folder = new Folder();
		folder.setId((long) 1);
		folder.setFsPath("/");
		folder.setUser(u);
		when(folderDao.get(any(Folder.class))).thenReturn(folder);
		
		fileService.uploadFile(u, "random0", folder, file);
		

		verify(filePathDao, times(1)).insertFilePath(any(FilePath.class));
		verify(fileDao, times(1)).insertFile(any(org.deel.domain.File.class));
		
		File f = new File ("/home/garulf/storage/nick/random0");
		Assert.assertTrue(f.exists());
		f.delete();
	}
	
	@Test
	public void listFileTest() {
		User u = new User();
		u.setUsername("nick");
		u.setId((long)1);
		
		
		Folder f = new Folder();
		f.setId((long)1);
		f.setName("/");
		f.setUser(u);
		
		Set<FilePath> files = new HashSet<FilePath>();
		FilePath fp1 = new FilePath();
		FilePath fp2 = new FilePath();
		
		fp1.setId((long)1);
		fp2.setId((long)2);
		
		fp1.setFile(null);
		fp2.setFile(null);
		
		fp1.setName("ciaocia");
		fp2.setName("asdasd");
		
		
		files.add(fp1);
		files.add(fp2);
		f.setFilepaths(files);
		
				
		Assert.assertEquals(fileService.getFilesInFolder(u, f),files);
		
	}
	
}
