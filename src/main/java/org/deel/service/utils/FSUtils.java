package org.deel.service.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.management.RuntimeErrorException;

import org.apache.commons.io.IOUtils;
import org.deel.domain.File;
import org.deel.domain.FileRevision;
import org.deel.domain.Folder;

public class FSUtils implements FileSystemGateway {

	private static String storagePath = System.getProperty("user.home")
			+ "/storage/";

	public static String getStoragePath() {
		return storagePath;
	}

	public void savePath(String path, InputStream inputStream) throws IOException {
		java.io.File fsF = new java.io.File(path);

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
	/* (non-Javadoc)
	 * @see org.deel.service.utils.FileSystemGateway#saveFile(org.deel.domain.FileRevision, java.io.InputStream)
	 */
	@Override
	public  void saveFile(FileRevision fileRevision,
			InputStream inputStream) throws IOException {

		String finalPath = storagePath
				+ fileRevision.getUploadedBy().getUsername()
				+ fileRevision.getFsPath() + "." + fileRevision.getId();

		savePath(finalPath, inputStream);
	}

	/* (non-Javadoc)
	 * @see org.deel.service.utils.FileSystemGateway#setStoragePath(java.lang.String)
	 */
	@Override
	public  void setStoragePath(String storagePath) {
		FSUtils.storagePath = storagePath;
	}

	/* (non-Javadoc)
	 * @see org.deel.service.utils.FileSystemGateway#mkdir(org.deel.domain.Folder)
	 */
	@Override
	public  void mkdir(Folder f) throws IOException {

		java.io.File dir = new java.io.File(storagePath
				+ f.getUser().getUsername() + f.getFsPath());
		if (!dir.mkdir())
			throw new RuntimeErrorException(new Error("directory.notcreated"),
					"Can't make dir" + dir.getAbsolutePath());
	}

	/* (non-Javadoc)
	 * @see org.deel.service.utils.FileSystemGateway#deleteFile(org.deel.domain.FileRevision)
	 */
	@Override
	public  void deleteFile(FileRevision f) {
		
		String finalPath = storagePath
				+ f.getUploadedBy().getUsername()
				+ f.getFsPath() + "." + f.getId();

		java.io.File fsF = new java.io.File(finalPath);
		
		if (fsF.isDirectory())
			throw new RuntimeException("DB/FS mismatch path "
					+ fsF.getAbsolutePath() + " is a directory!");
		
		fsF.delete();
	}
	
	/* (non-Javadoc)
	 * @see org.deel.service.utils.FileSystemGateway#deleteFolder(org.deel.domain.Folder)
	 */
	@Override
	public  void deleteFolder(Folder f) {
		
		java.io.File dir = new java.io.File(storagePath
				+ f.getUser().getUsername() + f.getFsPath());

	
		if (!dir.isDirectory())
			throw new RuntimeException("DB/FS mismatch path "
					+ dir.getAbsolutePath() + " is not a directory!");
		
		dir.delete();
	}

	/* (non-Javadoc)
	 * @see org.deel.service.utils.FileSystemGateway#mv(java.lang.String, java.lang.String)
	 */
	@Override
	public  void mv(String oldPath, String newPath) throws IOException {
		java.io.File f = new java.io.File(storagePath + oldPath);
		java.io.File nf = new java.io.File(storagePath + newPath);

		if (!f.exists())
			throw new RuntimeException("Error mv " + oldPath
					+ " does not exists!");

		if (f.isDirectory())
			throw new RuntimeException("Error mv " + oldPath
					+ " is a directory!");

		FileInputStream fIn = new FileInputStream(f);
		FileOutputStream fOut = new FileOutputStream(nf);
		IOUtils.copy(fIn, fOut);
		fIn.close();
		fOut.close();

		f.delete();

	}

	/* (non-Javadoc)
	 * @see org.deel.service.utils.FileSystemGateway#getFile(org.deel.domain.FileRevision)
	 */
	@Override
	public  FileInputStream getFile(FileRevision last) throws FileNotFoundException {
		String path = storagePath + last.getUploadedBy().getUsername()
				+ last.getFsPath() + "." + last.getId();

		java.io.File fsFile = new java.io.File(path);

		if (!fsFile.exists())
			throw new RuntimeException("DB/FS mismatch: file " + path
					+ " doesn't exists");

		FileInputStream fIn = new FileInputStream(fsFile);


		return fIn;
	}

}
