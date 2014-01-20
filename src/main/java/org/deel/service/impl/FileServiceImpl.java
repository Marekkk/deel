package org.deel.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.RuntimeErrorException;
import javax.transaction.Transactional;

import org.apache.commons.io.IOUtils;
import org.deel.domain.File;
import org.deel.dao.FileDAO;
import org.deel.dao.FilePathDAO;
import org.deel.dao.FolderDAO;
import org.deel.dao.UserDAO;
import org.deel.domain.DirectoryListing;
import org.deel.domain.FilePath;
import org.deel.domain.Folder;
import org.deel.domain.User;
import org.deel.service.FileService;
import org.deel.service.utils.FSUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class FileServiceImpl implements FileService {
	private String storagePath = System.getProperty("user.home") + "/storage/";
	private FolderDAO folderDao;
	private FilePathDAO filePathDao;
	private FileDAO fileDao;

	public UserDAO getUserDAO() {
		return userDAO;
	}

	@Autowired
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	private UserDAO userDAO;

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
	@Transactional
	public FileInputStream getFile(User currentUser, FilePath fp)
			throws FileNotFoundException {
		fp = filePathDao.getFilePath(fp);

		if (fp == null)
			throw new RuntimeException("Filepath doesn't exists");
		if (fp.getUser().getId() != currentUser.getId())
			throw new RuntimeException("Filepath " + fp.getName()
					+ "doesn't belongs to user " + currentUser.getUsername());
		String path = storagePath + currentUser.getUsername()
				+ fp.getFile().getFsPath();

		java.io.File file = new java.io.File(path);

		if (!file.exists())
			throw new RuntimeException("DB/FS mismatch: file " + path
					+ " doesn't exists");

		FileInputStream fIn = new FileInputStream(file);

		return fIn;
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
			throw new RuntimeException("DB/FS mismatch path "
					+ fsF.getAbsolutePath() + " is a directory!");

		FileOutputStream fOut;
		try {
			if (!fsF.createNewFile())
				throw new RuntimeException("DB/FS mismatch saving file "
						+ fsF.getAbsolutePath());

			fOut = new FileOutputStream(fsF);
			IOUtils.copy(inputStream, fOut);
			fOut.flush();
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
			if (fp.getName().equals(originalFilename)) {
				updateFile(curr, fp, inputStream);
				return;
			}

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
	@Transactional
	public Folder populateFolder(User curr, Folder folder) {
		/* if null return root directory */
		curr = userDAO.get(curr);
		if (folder.getId() == null) {
			Set<Folder> fl = curr.getFolders();
			for (Folder f : fl) {
				if (f.getFather() == null)
					return f;
			}
		}

		folder = folderDao.get(folder);
		if (folder == null)
			throw new RuntimeException("folder doesn't exists");

		if (folder.getUser().getId() != curr.getId())
			throw new RuntimeException("user " + curr.getUsername()
					+ " doesn't own folder " + folder.getFsPath());

		return folder;
	}

	@Override
	@Transactional
	public void createNewFolder(User u, Folder currentFolder, String dirName)
			throws IOException {
		currentFolder = populateFolder(u, currentFolder);

		Set<Folder> folderList = currentFolder.getInFolder();
		for (Folder f : folderList)
			if (f.getName() == dirName)
				throw new RuntimeException("Directory " + dirName
						+ " already exists!");

		for (FilePath fp : currentFolder.getFilepaths())
			if (fp.getName() == dirName)
				throw new RuntimeException("A file with name " + dirName
						+ " already exists!");

		Folder newFolder = new Folder();
		newFolder.setName(dirName);
		newFolder.setFather(currentFolder);
		newFolder.setFsPath(currentFolder.getFsPath() + dirName + "/"); /*
																		 * TODO
																		 * validation
																		 * of
																		 * this
																		 * input
																		 * (skip
																		 * slash
																		 * ,
																		 * points
																		 * )
																		 */
		newFolder.setUser(u);

		folderDao.insertFolder(newFolder);
		FSUtils.mkdir(newFolder);

	}

	@Override
	@Transactional
	public void deleteFile(User u, FilePath fp) throws IOException {
		fp = filePathDao.getFilePath(fp);

		if (fp == null)
			throw new RuntimeException(
					"Tried to erase a Filepath that doesn't exists");

		if (fp.getUser().getId() != u.getId())
			throw new RuntimeException("User " + u.getUsername()
					+ "doesn't own file " + fp.getName());

		File f = fp.getFile();

		boolean shared = !(f.getPaths().size() == 1);

		if (!shared) {
			/* file is not shared */
			filePathDao.deleteFilePath(fp);
			fileDao.deleteFile(f);
			FSUtils.deleteFile(f);
		} else {
			if (f.getOwner().getId() != u.getId()) {
				/* User current not the owner */
				filePathDao.deleteFilePath(fp);
				return;
			}
			/* i'm the owner */
			f.getPaths().remove(fp);
			filePathDao.deleteFilePath(fp);

			FilePath filePath = f.getPaths().iterator().next();

			String oldPath = f.getOwner().getUsername() + f.getFsPath();
			String newPath = filePath.getUser().getUsername()
					+ filePath.getFolder().getFsPath() + filePath.getName();

			f.setFsPath(newPath);
			f.setName(filePath.getName());
			f.setOwner(filePath.getUser());

			FSUtils.mv(oldPath, newPath);

		}

	}

	@Transactional
	@Override
	public void shareFile(User currentUser, FilePath filePath,
			List<User> userList) {
		filePath = filePathDao.getFilePath(filePath);
		if (filePath == null)
			throw new RuntimeException(
					"Error sharing a filepath that doesn't exists");

		if (filePath.getUser().getId() != currentUser.getId())
			throw new RuntimeException("User " + currentUser.getUsername()
					+ " doesn't own filepath " + filePath.getName());

		for (User user : userList) {
			user = userDAO.get(user);
			Folder root = null;

			for (Folder f : user.getFolders()) {
				if (f.getFather() == null) {
					root = f;
					return;
				}
			}

			FilePath nfp = new FilePath();

			nfp.setFile(filePath.getFile());
			nfp.setName(filePath.getName());
			nfp.setFolder(root);

			filePathDao.insertFilePath(nfp);
		}
		return;

	}

	@Override
	@Transactional
	public DirectoryListing listFolder(User curr, Folder folder) {
		/* we've to reload/merge curr if we want use lazy loading */
		curr = userDAO.get(curr);

		/* if null return root directory */
		if (folder.getId() == null) {
			Set<Folder> fl = curr.getFolders();
			for (Folder f : fl) {
				if (f.getFather() == null) {
					folder = f;
					break;
				}
			}
		} else {

			folder = folderDao.get(folder);
		}
		
		if (folder == null)
			throw new RuntimeException("folder doesn't exists");

		if (folder.getUser().getId() != curr.getId())
			throw new RuntimeException("user " + curr.getUsername()
					+ " doesn't own folder " + folder.getFsPath());

		DirectoryListing ret = new DirectoryListing();
		ret.setMe(folder);
		
		Set<FilePath> fpaths = new HashSet<FilePath>();
		fpaths.addAll(folder.getFilepaths());
		
		Set<Folder> folders = new HashSet<Folder>();
		folders.addAll(folder.getInFolder());
		
		ret.setFilePaths(fpaths);
		ret.setFolders(folders);
		
		return ret;
	}

}
