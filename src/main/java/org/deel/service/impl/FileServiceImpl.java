package org.deel.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.deel.dao.FileDao;
import org.deel.dao.FilePathDao;
import org.deel.dao.FolderDao;
import org.deel.domain.FilePath;
import org.deel.domain.Folder;
import org.deel.domain.User;
import org.deel.exception.FileAlreadyExistsException;
import org.deel.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;

public class FileServiceImpl implements FileService {

	private FolderDao folderDao;
	private FilePathDao filePathDao;	

	@Autowired
	public void setFilePathDao(FilePathDao filePathDao) {
		this.filePathDao = filePathDao;
	}
	public FilePathDao getFilePathDao() {
		return filePathDao;
	}

	public FolderDao getFolderDao() {
		return folderDao;
	}

	@Autowired
	public void setFolderDao(FolderDao folderDao) {
		this.folderDao = folderDao;
	}

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

	
	private void saveFileOnFilesystem(User curr, 
			String originalFilename, 
			String path, 
			InputStream inputStream) throws IOException{

		String finalPath = storagePath + curr.getUsername() + path
				+ originalFilename;

		
		File f = new File(finalPath);		

		if (f.isDirectory()) {
			throw new RuntimeException("Uploaded file has the same name of a directory");
		}

		Folder folder = folderDao.getFolder(path, curr);
		
		if (folder == null) {
			throw new RuntimeException("Directory doesn't exist");
		}

		FileOutputStream fOut;
		try {
			if (!f.createNewFile()) 
				throw new FileAlreadyExistsException();
				
			
			byte[] buff = new byte[1024];
			fOut = new FileOutputStream(f);
			while (inputStream.read(buff) != -1) {
				fOut.write(buff);
			}

			fOut.close();

		} catch (FileNotFoundException e) {
			/* should never happen because of f.createnewFile*/
			throw  new IOException();
		}

	}
	@Override
	public void saveNewFile(User curr,
			String originalFilename,
			String path,
			InputStream inputStream) throws IOException {

		
		try {
			saveFileOnFilesystem(curr, originalFilename, path, inputStream);
		} catch (FileAlreadyExistsException e) {
			/* If the file already exist just update it */
			updateFile(curr, path+originalFilename, inputStream);
			return;
		}
		
		
		org.deel.domain.File file = new org.deel.domain.File(originalFilename, curr);
		Folder folder = folderDao.loadFolderByPath(path, curr);
		FilePath filePath = new FilePath(path, curr, file, folder);
		
		filePathDao.insertFilePath(filePath);
		
	}





}
