package org.deel.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.deel.domain.FileRevision;
import org.deel.domain.Folder;
import org.deel.service.utils.FileSystemGateway;

public class FileGatewayStub implements FileSystemGateway {

	@Override
	public void savePath(String path, InputStream is) {
		System.out.println("**** in savePath *****");
		java.io.File f = new File(path);

		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		return;

	}

	public void saveFile(FileRevision fileRevision, InputStream inputStream)
			throws IOException {
		savePath("/home/garulf/test/" + fileRevision.getFsPath(), inputStream);

	}

	@Override
	public void setStoragePath(String storagePath) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mkdir(String path) throws IOException {
	}


	public void deleteFile(FileRevision f) {
		// TODO Auto-generated method stub

	}


	public void deleteFolder(Folder f) {
		// TODO Auto-generated method stub

	}


	public void mv(String oldPath, String newPath) throws IOException {
		// TODO Auto-generated method stub

	}


	public FileInputStream getFile(FileRevision last)
			throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFile(String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteFolder(String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public FileInputStream getFile(String path) throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
