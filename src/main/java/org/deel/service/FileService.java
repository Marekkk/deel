package org.deel.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.deel.domain.FilePath;
import org.deel.domain.Folder;
import org.deel.domain.User;
import org.springframework.transaction.annotation.Transactional;

public interface FileService {
	public String storagePath = "/home/garulf/info/esami/AE/code/storage/";
	
	@Transactional
	public void eraseFile(User owner, String path);
	
	@Transactional
	public void updateFile(User owner, FilePath filePath, InputStream data);
	
	@Transactional
	public void shareFile(User currentUser, String path, List<User> userList);

	@Transactional
	public InputStream getFile(User currentUser, String path);
	
	@Transactional
	public List<FilePath> listFile(User currentUser, String Path);

	@Transactional
	public void uploadFile(User curr, String originalFilename, Folder folder,
			InputStream inputStream) throws IOException;
}
