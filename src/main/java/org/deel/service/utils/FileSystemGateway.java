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

	public void mkdir(String path) throws IOException;

	public void deleteFile(FileRevision f);

	public void deleteFolder(Folder f);

	public FileInputStream getFile(FileRevision last)
			throws FileNotFoundException;
	
	public void savePath(String path, InputStream in) throws IOException;

}