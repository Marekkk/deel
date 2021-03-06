package org.deel.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.deel.domain.FilePathInfo;
import org.deel.domain.File;
import org.deel.dao.FileDAO;
import org.deel.dao.FilePathDAO;
import org.deel.dao.FileRevisionDAO;
import org.deel.dao.FolderDAO;
import org.deel.dao.TeamDAO;
import org.deel.dao.UserDAO;
import org.deel.domain.DirectoryListing;
import org.deel.domain.FilePath;
import org.deel.domain.FileRevision;
import org.deel.domain.Folder;
import org.deel.domain.FolderInfo;
import org.deel.domain.Team;
import org.deel.domain.User;
import org.deel.service.FileService;
import org.deel.service.utils.FileSystemGateway;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronizationManager;



public class FileServiceImpl implements FileService {
	private FileDAO fileDao;
	private FolderDAO folderDao;
	private FilePathDAO filePathDao;
	private FileRevisionDAO fileRevisionDAO;
	
	private FileSystemGateway fileSystemMapper;

	public FileSystemGateway getFileSystemGateway() {
		return fileSystemMapper;
	}

	@Autowired
	public void setFileSystemMapper(FileSystemGateway fileSystemMapper) {
		this.fileSystemMapper = fileSystemMapper;
	}

	public FileRevisionDAO getFileRevisionDAO() {
		return fileRevisionDAO;
	}

	@Autowired
	public void setFileRevisionDAO(FileRevisionDAO fileRevisionDAO) {
		this.fileRevisionDAO = fileRevisionDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	@Autowired
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	private UserDAO userDAO;
	private TeamDAO teamDAO;

	public TeamDAO getTeamDAO() {
		return teamDAO;
	}

	@Autowired
	public void setTeamDAO(TeamDAO teamDAO) {
		this.teamDAO = teamDAO;
	}

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
	public FilePathInfo updateFile(User u, FilePath fp, InputStream data, Long size)
			throws IOException {
		fp = filePathDao.getFilePath(fp);

		if (fp == null)
			throw new RuntimeException(
					"Tried to update a Filepath that doesn't exists");

		if (fp.getUser().getId() != u.getId())
			throw new RuntimeException("User " + u.getUsername()
					+ "doesn't own file " + fp.getName());

		FileRevision newRevision = new FileRevision();
		newRevision.setDate(new Date());
		newRevision.setFsPath(fp.getFolder().getFsPath() + fp.getName());
		newRevision.setFile(fp.getFile());
		newRevision.setUploadedBy(u);
		newRevision.setSize(size);

		fileRevisionDAO.insert(newRevision);
		
			
		
		
		File f = fp.getFile();
		List<FileRevision> fake = new LinkedList<FileRevision>();
		fake.add(newRevision);
		
		f.setRevisions(fake);
		
		
		FilePathInfo fInfo = new FilePathInfo(fp);
		
		
		fileSystemMapper.savePath(newRevision.getCompleteFsPath(), data);
		
		
		return fInfo;

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

		/* Revision list is ordered */
		FileRevision last = fp.getFile().getRevisions().get(0);
		//
		// for (FileRevision fileRevision : file.getRevisions())
		// if (fileRevision.getId() > last.getId())
		// last = fileRevision;
		//

		
		
		return fileSystemMapper.getFile(last.getCompleteFsPath());
	}

	@Override
	@Transactional
	public FilePathInfo uploadFile(User curr, String originalFilename, Folder folder,
			InputStream inputStream, Long size) throws IOException {

 
		folder = folderDao.get(folder);

		if (folder == null)
			throw new RuntimeException("folder id doesn't exist");

		if (folder.getUser().getId() != curr.getId())
			throw new RuntimeException("User doesn't own the folder "
					+ folder.getFsPath() + "with id" + folder.getId());

		if(folder.existsFolder(originalFilename))
			throw new RuntimeException(
						"Uploaded file has the same name of a directory");

		FilePath filepath = folder.getFilePathByName(originalFilename);
		if (filepath != null)
			return updateFile(curr, filepath, inputStream, size);

		/*TODO we should do better hibernaete mapping of relations*/		
		FileRevision fileRevision = new FileRevision();
		fileRevision.setDate(new Date());
		fileRevision.setFsPath(folder.getFsPath() + originalFilename);
		fileRevision.setUploadedBy(curr);
		fileRevision.setSize(size);
		

		File file = new File();

		file.setName(originalFilename);
		file.setOwner(curr);

		fileRevision.setFile(file);

		// file.setFsPath(folder.getFsPath() + originalFilename);

		FilePath fp = new FilePath();
		fp.setFile(file);
		fp.setFolder(folder);
		fp.setName(originalFilename);
		fp.setUser(curr);
		fp.setHidden(false);

		fileDao.insertFile(file);
		fileRevisionDAO.insert(fileRevision);
		filePathDao.insertFilePath(fp);

		List<FileRevision> fakeRevisionList = new LinkedList<FileRevision>();
		fakeRevisionList.add(fileRevision);
		fp.setFile(file);
		file.setRevisions(fakeRevisionList);
		
		FilePathInfo fInfo = new FilePathInfo(fp);
		
		fileSystemMapper.savePath(fileRevision.getCompleteFsPath(), inputStream);
		
		
		return fInfo;

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
	public FolderInfo createNewFolder(User u, Folder currentFolder, String dirName)
			throws IOException {
		currentFolder = folderDao.get(currentFolder);
		if (currentFolder == null)
			throw new RuntimeException("folder doesn't exists");

		if (currentFolder.getUser().getId() != u.getId())
			throw new RuntimeException("user " + u.getUsername()
					+ " doesn't own folder " + currentFolder.getFsPath());
		

		Set<Folder> folderList = currentFolder.getInFolder();
		for (Folder f : folderList)
			if (f.getName().equals(dirName))
				throw new RuntimeException("Directory " + dirName
						+ " already exists!");

		for (FilePath fp : currentFolder.getFilepaths())
			if (fp.getName().equals(dirName))
				throw new RuntimeException("A file with name " + dirName
						+ " already exists!");

		Folder newFolder = new Folder();
		newFolder.setName(dirName);
		newFolder.setFather(currentFolder);
		newFolder.setUser(u);
		newFolder.setHidden(false);

		folderDao.insertFolder(newFolder);

		newFolder.setFsPath(currentFolder.getFsPath() + dirName + "."
				+ newFolder.getId() + "/");

		folderDao.updateFolder(newFolder);

		/*
		 * TODO validation of this input (skip slash , points )
		 */

		FolderInfo fInfo = new FolderInfo(newFolder);
		fileSystemMapper.mkdir(newFolder.getCompleteFSPath());
		return fInfo;

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
		
		fp.setHidden(true);
		filePathDao.updateFilePath(fp);
	}

	@Transactional
	@Override
	public void shareFile(User currentUser, FilePath filePath,
			List<User> userList, List<Team> teamList) {
		
		filePath = filePathDao.getFilePath(filePath);
		if (filePath == null)
			throw new RuntimeException(
					"Error sharing a filepath that doesn't exists");

		if (filePath.getUser().getId() != currentUser.getId())
			throw new RuntimeException("User " + currentUser.getUsername()
					+ " doesn't own filepath " + filePath.getName());

		List<User> pUL = new LinkedList<User>();
		
		for (Team team : teamList) {
			team = teamDAO.get(team);
			if (team == null)
				throw new RuntimeException(
						"Trying to share a file with a non existent user!");
		
			pUL.addAll(team.getUsersInTeam());

		}

		for (User user : userList) {
			user = userDAO.get(user);
			if (user == null)
				throw new RuntimeException(
						"Trying to share a file with a non existent user!");
			pUL.add(user);
		}
		
		for (User user : userList) {
			user = userDAO.get(user);
			if (user == null)
				throw new RuntimeException(
						"Trying to share a file with a non existent user!");

			Folder root = null;

			for (Folder f : user.getFolders()) {
				if (f.getFather() == null) {
					root = f;
					break;
				}
			}

			FilePath nfp = new FilePath();

			nfp.setFile(filePath.getFile());
			nfp.setName(filePath.getName());
			nfp.setFolder(root);
			nfp.setUser(user);
			nfp.setHidden(false);

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
		ret.setMe(new FolderInfo(folder));

		Set<FilePathInfo> fpaths = new HashSet<FilePathInfo>();
		for (FilePath filePath : folder.getFilepaths()) 
			fpaths.add(new FilePathInfo(filePath));
		
		Set<FolderInfo> folders = new HashSet<FolderInfo>();
		for (Folder f : folder.getInFolder()) 
			folders.add(new FolderInfo(f));
		


		ret.setFilePaths(fpaths);
		ret.setFolders(folders);

		return ret;
	}

	@Override
	@Transactional
	public void deleteFolder(User u, Folder folder) {

		folder = folderDao.get(folder);

		if (folder == null)
			throw new RuntimeException("folder doesn't exists");
		if (folder.getUser().getId() != u.getId())
			throw new RuntimeException("User " + u.getUsername()
					+ "doesn't own folder " + folder.getName());

		List<Folder> folderToDelete = new LinkedList<Folder>();
		folderToDelete.add(folder);

		for (int i = 0; i < folderToDelete.size(); i++) {
			Folder f = folderToDelete.get(i);

			/* Deleting all filepaths */
			for (FilePath fp : f.getFilepaths()) {
				if (fp.isHidden())
					continue;

				fp.setHidden(true);
			}

			/* Recursing */
			for (Folder dir : f.getInFolder()) {
				if (dir.isHidden())
					continue;

				folderToDelete.add(dir);
			}
			f.setHidden(true);
			folderDao.updateFolder(folder);

		}

	}

	@Override
	@Transactional
	public List<FileRevision> getRevisionList(User curr, FilePath fp) {
		fp = filePathDao.getFilePath(fp);

		if (fp == null)
			throw new RuntimeException("filePath doesn't exists");

		if (fp.getUser().getId() != curr.getId())
			throw new RuntimeException("User " + curr.getUsername()
					+ " doesn't own filepath " + fp.getName());

		fp.getFile().getRevisions().get(0);
		/* they are already ordered */
		Hibernate.initialize(fp.getFile().getRevisions());
		return fp.getFile().getRevisions();
	}

	@Override
	@Transactional
	public FileInputStream getRevision(User curr, FilePath filePath,
			FileRevision fileRevision) throws FileNotFoundException {
		filePath = filePathDao.getFilePath(filePath);

		if (filePath == null)
			throw new RuntimeException("filePath doesn't exists");

		if (filePath.getUser().getId() != curr.getId())
			throw new RuntimeException("User " + curr.getUsername()
					+ " doesn't own filepath " + filePath.getName());

		List<FileRevision> frl = filePath.getFile().getRevisions();

		FileRevision pFileRevision = null;

		for (FileRevision fr : frl) {
			if (fr.getId().equals(fileRevision.getId())) {
				pFileRevision = fr;
				break;
			}
		}

		if (pFileRevision == null)
			throw new RuntimeException(
					"Not enough permission to download filerevision or filerevision doesn't exists");

		return fileSystemMapper.getFile(pFileRevision.getCompleteFsPath());
	}

	@Override
	@Transactional
	public void deleteFromTrash(FilePath f, User curr) {
		f = filePathDao.getFilePath(f);

		if (f == null)
			throw new RuntimeException("filePath doesn't exists");

		if (f.getUser().getId() != curr.getId())
			throw new RuntimeException("User " + curr.getUsername()
					+ " doesn't own filepath " + f.getName());
		
		File file = f.getFile();
		Set<FilePath> paths = file.getPaths();
		
		// If I' m the owner delete all
		if (file.getOwner().getId() == curr.getId()) {
			List<FileRevision> revisions = file.getRevisions();
			for (FileRevision fileRevision : revisions) {
				fileRevisionDAO.delete(fileRevision);
				/* TODO should be moved after all db interaction */
				fileSystemMapper.deleteFile(fileRevision.getCompleteFsPath());
			}
			for (FilePath filePath : paths) {
				filePathDao.deleteFilePath(filePath);
			}
			
			fileDao.deleteFile(file);
			System.out.println("The file has been deleted.");
		}
		else if (file.getOwner().getId() != curr.getId()) {
			List<FileRevision> revisions = file.getRevisions();
			for (FileRevision fileRevision : revisions) {
				if (fileRevision.getUploadedBy().getId() == curr.getId()) {
					fileRevisionDAO.delete(fileRevision);
					fileSystemMapper.deleteFile(fileRevision.getCompleteFsPath());
				}
			}
			file.getPaths().remove(f);
			filePathDao.deleteFilePath(f);
		}
		}

	@Override
	@Transactional
	public void deleteFolderFromTrash(Folder folder, User curr) {
		folder = folderDao.get(folder);
		
		if (folder == null)
			throw new RuntimeException("folder doesn't exists");
		
		if (folder.getUser().getId() != curr.getId())
			throw new RuntimeException("User " + curr.getUsername()
					+ " doesn't own folder " + folder.getName());
		
		Set<FilePath> paths = folder.getFilepaths();
		Set<Folder> folders = folder.getInFolder();
		
		for (FilePath fp : paths) {
			this.deleteFromTrash(fp, curr);
		}
		
		for (Folder f : folders) {
			this.deleteFolderFromTrash(f, curr);
		}
		
		folderDao.deleteFolder(folder);
		fileSystemMapper.deleteFolder(folder.getCompleteFSPath());
	}	

}
