package org.deel.service.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import javax.management.RuntimeErrorException;
import javax.transaction.Transactional;

import org.deel.domain.File;
import org.deel.dao.FileDAO;
import org.deel.dao.FilePathDAO;
import org.deel.dao.FolderDAO;
import org.deel.domain.FilePath;
import org.deel.domain.Folder;
import org.deel.domain.User;
import org.deel.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;

public class FileServiceImpl implements FileService {
	private String storagePath = System.getProperty("user.home") + "/storage/";
	private FolderDAO folderDao;
	private FilePathDAO filePathDao;
	private FileDAO fileDao;

	public FileDAO getFileDao() {
		return fileDao;
	}

	@Autowired
	public void setFileDao(FileDAO fileDao) {
		this.fileDao = fileDao;
	}

	@Autowired
	public void setFilePathDao(FilePathDAO filePathDao) {
		this.filePathDao = filePathDao;
	}

	public FilePathDAO getFilePathDao() {
		return filePathDao;
	}

	public FolderDAO getFolderDao() {
		return folderDao;
	}

	@Autowired
	public void setFolderDao(FolderDAO folderDao) {
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
	@Transactional
	public Set<FilePath> getFilesInFolder(User currentUser, Folder f) {
		

		if (f == null)
			throw new RuntimeException("Directory doesn't exists");

		if (f.getUser().getId() != currentUser.getId())
			throw new RuntimeException("User doesn't own the directory!");

		Set<FilePath> ret = f.getFilepaths();

		return ret;
	}

	@Override
	public Set<Folder> getFoldersInFolder(User currentUser, Folder f) {


		if (f == null)
			throw new RuntimeException("Directory doesn't exists");

		if (f.getUser().getId() != currentUser.getId())
			throw new RuntimeException("User doesn't own the directory!");
		
		Set<Folder> ret = f.getInFolder();

		return ret;
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

	private void saveFileOnFilesystem(File f, InputStream inputStream)
			throws IOException {

		String finalPath = storagePath + f.getOwner().getUsername()
				+ f.getFsPath();

		java.io.File fsF = new java.io.File(finalPath);

		if (fsF.isDirectory())
			throw new RuntimeException("DB/FS mismatch path " + fsF.getAbsolutePath() + " is a directory!");

		FileOutputStream fOut;
		try {
			if (!fsF.createNewFile())
				throw new RuntimeException("DB/FS mismatch saving file " + fsF.getAbsolutePath());

			byte[] buff = new byte[1024];
			fOut = new FileOutputStream(fsF);
			while (inputStream.read(buff) != -1) {
				fOut.write(buff);
			}

			fOut.close();

		} catch (FileNotFoundException e) {
			/* should never happen because of f.createnewFile */
			throw new RuntimeException("DB/FS mismatch");

		}
	}

	@Override
	@Transactional
	public void uploadFile(User curr, String originalFilename, Folder folder,
			InputStream inputStream) throws IOException {

		folder = folderDao.get(folder);

		if (folder == null)
			throw new RuntimeException("folder id doesn't exist");

		if (folder.getUser().getId() != curr.getId())
			throw new RuntimeException("User doesn't own the folder "
					+ folder.getFsPath() + "with id" + folder.getId());

		for (Folder f : folder.getInFolder())
			if (f.getName() == originalFilename)
				throw new RuntimeException(
						"Uploaded file has the same name of a directory");

		/* TODO change Filepath.path in FilePath.name */
		for (FilePath fp : folder.getFilepaths())
			if (fp.getName() == originalFilename)
				updateFile(curr, fp, inputStream);

		File file = new File();

		file.setName(originalFilename);
		file.setOwner(curr);
		file.setFsPath(folder.getFsPath() + originalFilename);

		fileDao.insertFile(file);

		FilePath fp = new FilePath();

		fp.setFile(file);
		fp.setFolder(folder);
		fp.setName(originalFilename);
		fp.setUser(curr);

		filePathDao.insertFilePath(fp);

		saveFileOnFilesystem(file, inputStream);

	}

	@Override
	public Folder populateFolder(User curr, Folder folder) {
		/* if null return root directory */
		if (folder.getId() == null) {
			Set<Folder> fl = curr.getFolders();
			for (Folder f: fl) {
				if(f.getFather() == null)
					return f;
			}
		}
		
		folder = folderDao.get(folder);
		if (folder == null)
			throw new RuntimeException("folder doesn't exists");
		
		if (folder.getUser().getId() != curr.getId())
			throw new RuntimeException("user " + curr.getUsername() + " doesn't own folder " + folder.getFsPath());
		
		return folder;
	}

}
