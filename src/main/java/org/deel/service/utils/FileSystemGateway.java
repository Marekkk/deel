package org.deel.service.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileSystemGateway {



	public void mkdir(String path) throws IOException;
	
	public void setStoragePath(String storagePath);


	public void deleteFile(String path);
	
	public void deleteFolder(String path);

	public FileInputStream getFile(String path) throws FileNotFoundException;
	
	public void savePath(String path, InputStream in) throws IOException;

}