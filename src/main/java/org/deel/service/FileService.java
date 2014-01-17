package org.deel.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import org.deel.domain.File;
import org.deel.domain.FilePath;
import org.deel.domain.Folder;
import org.deel.domain.User;
import org.springframework.transaction.annotation.Transactional;

public interface FileService {
	
	
	@Transactional
	public void eraseFile(User owner, String path);
	
	@Transactional
	public void updateFile(User owner, FilePath filePath, InputStream data);
	
	@Transactional
	public void shareFile(User currentUser, String path, List<User> userList);

	@Transactional
	public FileInputStream getFile(User currentUser, FilePath filePath) throws FileNotFoundException;
	
	@Transactional
	public List<FilePath> listFile(User currentUser, String Path);

	@Transactional
	public void uploadFile(User curr, String originalFilename, Folder folder,
			InputStream inputStream) throws IOException;
	
	@Transactional
	public Set<Folder> getFoldersInFolder(User currentUser, Folder f);
	
	@Transactional
	public Set<FilePath> getFilesInFolder(User currentUser, Folder f);

	@Transactional
	public Folder populateFolder(User currentUser, Folder folder);
}
