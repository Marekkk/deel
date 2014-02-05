package org.deel.service.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.deel.domain.FileRevision;
import org.deel.domain.Folder;

public interface FileSystemGateway {

	public void saveFile(FileRevision fileRevision, InputStream inputStream)
			throws IOException;

	public void setStoragePath(String storagePath);

	public void mkdir(Folder f) throws IOException;

	public void deleteFile(FileRevision f);

	public void deleteFolder(Folder f);

	public void mv(String oldPath, String newPath) throws IOException;

	public FileInputStream getFile(FileRevision last)
			throws FileNotFoundException;
	
	public void savePath(String path, InputStream in) throws IOException;

}