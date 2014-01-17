package org.deel.service.impl;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.transaction.Transactional;

import org.deel.domain.File;
import org.deel.dao.FileDao;
import org.deel.dao.FilePathDao;
import org.deel.dao.FolderDao;
import org.deel.domain.FilePath;
import org.deel.domain.Folder;
import org.deel.domain.User;
import org.deel.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;

public class FileServiceImpl implements FileService {

	private FolderDao folderDao;
	private FilePathDao filePathDao;
	private FileDao fileDao;

	public FileDao getFileDao() {
		return fileDao;
	}
	
	@Autowired
	public void setFileDao(FileDao fileDao) {
		this.fileDao = fileDao;
	}
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
	public void updateFile(User owner, FilePath fp, InputStream data) {
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

	
	private void saveFileOnFilesystem(File f, 
			InputStream inputStream) throws IOException{

		
		String finalPath = storagePath + f.getOwner().getUsername() +  f.getFSPath();

		
		java.io.File fsF = new java.io.File(finalPath);		

		if (fsF.isDirectory()) 
			throw new RuntimeException("DB/FS mismatch");
		
		FileOutputStream fOut;
		try {
			if (!fsF.createNewFile()) 
				throw new RuntimeException("DB/FS mismatch");
				
			byte[] buff = new byte[1024];
			fOut = new FileOutputStream(fsF);
			while (inputStream.read(buff) != -1) {
				fOut.write(buff);
			}

			fOut.close();

		} catch (FileNotFoundException e) {
			/* should never happen because of f.createnewFile*/
			throw new RuntimeException("DB/FS mismatch");

		}
	}
	
	@Override
	@Transactional
	public void uploadFile(User curr,
			String originalFilename,
			Folder folder,
			InputStream inputStream) throws IOException {

		folder = folderDao.get(folder);
		
		// TODO check if current user has this folder
		for (Folder f : folder.getInFolder()) 
			if (f.getName() == originalFilename)
				throw new RuntimeException("Uploaded file has the same name of a directory");
		
		/* TODO change Filepath.path in FilePath.name */
		for (FilePath fp : folder.getFilepaths()) 
			if (fp.getName() == originalFilename)
				updateFile(curr, fp, inputStream);
		

		File file = new File();
		
		file.setName(originalFilename);
		file.setOwner(curr);
		file.setFSPath(folder.getFSPath()+originalFilename);
			
		fileDao.insertFile(file);
		
		FilePath fp = new FilePath();
		
		fp.setFile(file);
		fp.setFolder(folder);
		fp.setName(originalFilename);
		fp.setUser(curr);
		
		filePathDao.insertFilePath(fp);

		saveFileOnFilesystem(file, inputStream);
				
	}





}
