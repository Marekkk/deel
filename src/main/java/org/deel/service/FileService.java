package org.deel.service;

import java.io.InputStream;
import java.util.List;

import org.deel.domain.FilePath;
import org.deel.domain.User;

public interface FileService {
	
	public void eraseFile(User owner, String path);
	
	public void updateFile(User owner, String path, InputStream data);
	
	public void shareFile(User currentUser, String path, List<User> userList);
	
	public InputStream getFile(User currentUser, String path);
	
	public List<FilePath> listFile(User currentUser, String Path);


	public void saveNewFile(User curr, String originalFilename, String path,
			byte[] bytes);
}
