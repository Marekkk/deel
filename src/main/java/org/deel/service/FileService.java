package org.deel.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import org.deel.domain.DirectoryListing;
import org.deel.domain.File;
import org.deel.domain.FilePath;
import org.deel.domain.Folder;
import org.deel.domain.User;
import org.springframework.transaction.annotation.Transactional;

public interface FileService {

	
	@Transactional
	public void updateFile(User owner, FilePath filePath, InputStream data) throws IOException;
	
	@Transactional
	public void shareFile(User currentUser, FilePath filePath, List<User> userList);

	@Transactional
	public FileInputStream getFile(User currentUser, FilePath filePath) throws FileNotFoundException;
	

	@Transactional
	public void uploadFile(User curr, String originalFilename, Folder folder,
			InputStream inputStream) throws IOException;
	

	@Transactional
	public Folder populateFolder(User currentUser, Folder folder);

	@Transactional
	public void deleteFolder(User u, Folder folder);
	
	@Transactional
	public void createNewFolder(User u, Folder currentFolder, String string) throws IOException;

	@Transactional
	public void deleteFile(User u, FilePath fdc) throws IOException;

	@Transactional
	public DirectoryListing listFolder(User curr, Folder folder);
}
