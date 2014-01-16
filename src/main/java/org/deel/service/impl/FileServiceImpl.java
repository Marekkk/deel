package org.deel.service.impl;

import java.io.InputStream;
import java.util.List;

import org.deel.domain.FilePath;
import org.deel.domain.User;
import org.deel.service.FileService;

public class FileServiceImpl implements FileService {

	@Override
	public void eraseFile(User owner, String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateFile(User owner, String path, InputStream data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void shareFile(User currentUser, String path, List<User> userList) {
		// TODO Auto-generated method stub

	}

	@Override
	public InputStream getFile(User currentUser, String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FilePath> listFile(User curretUser, String Path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveNewFile(User curr, String originalFilename, String path,
			byte[] bytes) {
		// TODO Auto-generated method stub

	}

}
