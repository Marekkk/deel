package org.deel.service.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.deel.domain.FileRevision;
import org.deel.domain.Folder;

public interface FileSystemGateway {



	public void setStoragePath(String storagePath);

	public void mkdir(String path) throws IOException;

	public void deleteFile(String path);

	public void deleteFolder(String path);

	public FileInputStream getFile(String path)
			throws FileNotFoundException;
	
	public void savePath(String path, InputStream in) throws IOException;

}