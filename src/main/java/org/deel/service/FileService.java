package org.deel.service;

import java.io.InputStream;
import java.util.List;

import org.deel.domain.FilePath;
import org.deel.domain.User;
import org.springframework.transaction.annotation.Transactional;

public interface FileService {
	
	@Transactional
	public void eraseFile(User owner, String path);
	
	@Transactional
	public void updateFile(User owner, String path, InputStream data);
	
	@Transactional
	public void shareFile(User currentUser, String path, List<User> userList);

	@Transactional
	public InputStream getFile(User currentUser, String path);
	
	@Transactional
	public List<FilePath> listFile(User currentUser, String Path);

	@Transactional
	public void saveNewFile(User curr, String originalFilename, String path,
			byte[] bytes);
}
