package org.deel.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.deel.domain.DirectoryListing;
import org.deel.domain.FilePath;
import org.deel.domain.FilePathInfo;
import org.deel.domain.FileRevision;
import org.deel.domain.Folder;
import org.deel.domain.FolderInfo;
import org.deel.domain.Team;
import org.deel.domain.User;
import org.deel.service.utils.FileSystemGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class FileServiceStub implements FileService {

	private FileSystemGateway fileSystemMapper;
	
	public FileSystemGateway getFileSystemMapper() {
		return fileSystemMapper;
	}

	@Autowired
	public void setFileSystemMapper(FileSystemGateway fileSystemMapper) {
		this.fileSystemMapper = fileSystemMapper;
	}

	@Override
	@Transactional
	public FilePathInfo updateFile(User owner, FilePath filePath,
			InputStream data, Long size) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public void shareFile(User currentUser, FilePath filePath,
			List<User> userList, List<Team> teamList) {
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional
	public FileInputStream getRevision(User currentUser, FilePath filePath,
			FileRevision fileRevision) throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public FileInputStream getFile(User currentUser, FilePath filePath)
			throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public FilePathInfo uploadFile(User curr, String originalFilename,
			Folder folder, InputStream inputStream, Long size)
			throws IOException {
		
		FileRevision fr = new FileRevision();
		fr.setFsPath(originalFilename);
		fileSystemMapper.savePath("/home/garulf/test/"+originalFilename, inputStream);
		
		throw new RuntimeException();
	}

	@Override
	@Transactional
	public Folder populateFolder(User currentUser, Folder folder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public void deleteFolder(User u, Folder folder) {
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional
	public FolderInfo createNewFolder(User u, Folder currentFolder,
			String string) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public void deleteFile(User u, FilePath fdc) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional
	public DirectoryListing listFolder(User curr, Folder folder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public List<FileRevision> getRevisionList(User curr, FilePath fp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public void deleteFromTrash(FilePath fp, User curr) {
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional
	public void deleteFolderFromTrash(Folder folder, User curr) {
		// TODO Auto-generated method stub

	}

}
