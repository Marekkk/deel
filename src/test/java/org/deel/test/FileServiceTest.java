package org.deel.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.deel.dao.FileDAO;
import org.deel.dao.FilePathDAO;
import org.deel.dao.FileRevisionDAO;
import org.deel.dao.FolderDAO;
import org.deel.dao.UserDAO;
import org.deel.domain.FilePath;
import org.deel.domain.FileRevision;
import org.deel.domain.Folder;
import org.deel.domain.User;
import org.deel.service.FileService;
import org.deel.service.impl.FileServiceImpl;
import org.deel.service.utils.FSUtils;
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
	
	@Mock
	private UserDAO userDAO;

	@Mock
	private FileRevisionDAO fileRevisionDao;

	@InjectMocks
	private FileService fileService;


	
	@Before
	public void setup() {
		fileService = new FileServiceImpl();
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void eraseFile() throws Exception {
		
		File f = new File(FSUtils.getStoragePath() + "nick/deleteTest");
		FileOutputStream fOut = new FileOutputStream(f);
		OutputStreamWriter bw = new OutputStreamWriter(fOut);
		bw.write("test");
		bw.flush();
		bw.close();
		fOut.flush();
		fOut.close();
		
		User u = new User();
		u.setUsername("nick");
		u.setId((long)1);
		
		User u2 = new User();
		u2.setUsername("kick");
		u2.setId((long)2);
		
		
		org.deel.domain.File dbFile = new org.deel.domain.File();
//		dbFile.setFsPath("/deleteTest");
		dbFile.setName("/deleteTest");
		dbFile.setOwner(u);
		
	
		FilePath fp = new FilePath();
		fp.setName("deleteTest");
		fp.setFile(dbFile);
		fp.setUser(u);
		fp.setId((long)1);
		Set<FilePath> fpl = new HashSet<FilePath>();
		fpl.add(fp);
		
		FilePath fp1 = new FilePath();
		fp1.setName("deleteSharedTest");
		fp1.setFile(dbFile);
		fp1.setUser(u2);
		fp1.setId((long)2);
		Set<FilePath> fpl1 = new HashSet<FilePath>();
		
		
		Set<FilePath> fps = new HashSet<FilePath>();
		fps.add(fp);
		fps.add(fp1);
		dbFile.setPaths(fps);
		
		
		Folder dir = new Folder();
		dir.setFsPath("/");
		dir.setUser(u);
		dir.setFilepaths(fpl);
		fp.setFolder(dir);

		Folder dir1 = new Folder();
		dir1.setFsPath("/");
		dir1.setUser(u2);
		dir1.setFilepaths(fpl1);
		fp1.setFolder(dir1);
		
		when(filePathDao.getFilePath(any(FilePath.class))).thenReturn(fp);

		org.deel.domain.FilePath fdc = new org.deel.domain.FilePath();
		fdc.setId((long)1);
		fileService.deleteFile(u,  fdc);
		
		verify(fileDao, never()).deleteFile(any(org.deel.domain.File.class));
		verify(filePathDao, times(1)).deleteFilePath(any(FilePath.class));
		
		File f1 = new File(FSUtils.getStoragePath() + "kick/deleteSharedTest");
		Assert.assertTrue(f1.exists());
		f1.delete();
		
	}
	
	@Test
	public void dbInteractionUploadTest() throws Exception {
		
		File fout = new File(System.getProperty("user.home") + "/test1");
		
		FileWriter fw = new FileWriter(fout);
		fw.write("test");
		fw.close();
		
		User u = new User();
		u.setUsername("nick");
		u.setId((long)1);
		
		Folder folder = new Folder();
		folder.setId((long) 1);
		folder.setFsPath("/");
		folder.setUser(u);
		when(folderDao.get(any(Folder.class))).thenReturn(folder);
		when(fileRevisionDao.insert(any(FileRevision.class))).thenReturn((long)1);
		
		fileService.uploadFile(u, "/test1", folder, new FileInputStream(fout));
		
		when(fileRevisionDao.insert(any(FileRevision.class))).thenReturn((long)2);
		
		fileService.uploadFile(u, "/test1", folder, new FileInputStream(fout));
		
		File f1 = new File (System.getProperty("user.home") + "/storage/nick/test1.1");
		File f2 = new File (System.getProperty("user.home") + "/storage/nick/test1.2");
		
		verify(filePathDao, times(1)).insertFilePath(any(FilePath.class));
		verify(fileDao, times(1)).insertFile(any(org.deel.domain.File.class));		
		verify(fileRevisionDao, times(2)).insert(any(org.deel.domain.FileRevision.class));
		
		Assert.assertTrue(f1.exists());
		Assert.assertTrue(f2.exists());
		
		f1.delete();
		f2.delete();
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
		
				
		
		
	}
	
	@Test
	public void createNewDirTest() throws Exception {
		
		User u = new User();
		u.setUsername("nick");
		u.setId((long)1);
		
		Folder currentFolder = new Folder();
		currentFolder.setFsPath("/");
		currentFolder.setId((long)1);
		currentFolder.setFilepaths(new HashSet<FilePath>());
		currentFolder.setUser(u);
		
		when(folderDao.get(any(Folder.class))).thenReturn(currentFolder);
		
		fileService.createNewFolder(u, currentFolder, "subdir");
		
		
		verify(folderDao).insertFolder(any(Folder.class));
		File f = new File(System.getProperty("user.home") + "/storage/nick/subdir");
		Assert.assertTrue(f.isDirectory());
		f.delete();
		
	}
	
	@Test
	public void getFileTest() throws IOException {
		
		
		User u = new User();
		u.setUsername("nick");
		u.setId((long)1);
		
		FilePath filePath = new FilePath();
		filePath.setId((long)3);
		filePath.setUser(u);
		
		org.deel.domain.File file = new org.deel.domain.File();
	//	file.setFsPath("/test");
		filePath.setFile(file);
		
		when(filePathDao.getFilePath(any(FilePath.class))).thenReturn(filePath);
		
		InputStream in = fileService.getFile(u, filePath);
		Assert.assertNotNull(in);
		
		BufferedReader inb = new BufferedReader(new InputStreamReader(in));
		String inputLine;
		while ((inputLine = inb.readLine()) != null)
		    System.out.println(inputLine);
		in.close();
		
		
		
	}
	
	@Test
	public void shareFile() throws Exception {
		User u = new User();
		u.setUsername("nick");
		u.setId((long)1);
		
		FilePath fp = new FilePath();
		fp.setUser(u);
		
		User us = new User();
		us.setUsername("kick");
		us.setId((long)2);
		
		List<User> ul = new LinkedList<User>();
		ul.add(us);
		when(filePathDao.getFilePath(any(FilePath.class))).thenReturn(fp);
		when(userDAO.get(any(User.class))).thenReturn(us);
		
		fileService.shareFile(u, fp, ul, null);
		
		verify(filePathDao).insertFilePath(any(FilePath.class));
		
	}
	
}
