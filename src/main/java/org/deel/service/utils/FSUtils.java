package org.deel.service.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.management.RuntimeErrorException;

import org.apache.commons.io.IOUtils;

public class FSUtils implements FileSystemGateway {

	private static String storagePath = System.getProperty("user.home")
			+ "/storage/";

	public static String getStoragePath() {
		return storagePath;
	}

	public void savePath(String path, InputStream inputStream) throws IOException {
		String finalPath = storagePath + path;
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
	public  void setStoragePath(String storagePath) {
		FSUtils.storagePath = storagePath;
	}

	/* (non-Javadoc)
	 * @see org.deel.service.utils.FileSystemGateway#mkdir(org.deel.domain.Folder)
	 */
	@Override
	public  void mkdir(String path) throws IOException {

		java.io.File dir = new java.io.File(storagePath + path);
		
		//f.getUser().getUsername() + f.getFsPath()
		if (!dir.mkdir())
			throw new RuntimeErrorException(new Error("directory.notcreated"),
					"Can't make dir" + dir.getAbsolutePath());
	}

	/* (non-Javadoc)
	 * @see org.deel.service.utils.FileSystemGateway#deleteFile(org.deel.domain.FileRevision)
	 */
	@Override
	public  void deleteFile(String path) {
		
		System.out.println("***** in delete file ******");
		
		String finalPath = storagePath + path;
//		
//				+ f.getUploadedBy().getUsername()
//				+ f.getFsPath() + "." + f.getId();

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
	public  void deleteFolder(String path) {
		
		java.io.File dir = new java.io.File(storagePath
				+ path);
				//f.getUser().getUsername() + f.getFsPath());

	
		if (!dir.isDirectory())
			throw new RuntimeException("DB/FS mismatch path "
					+ dir.getAbsolutePath() + " is not a directory!");
		
		dir.delete();
	}

	/* (non-Javadoc)
	 * @see org.deel.service.utils.FileSystemGateway#mv(java.lang.String, java.lang.String)
	 */
	public static void mv(String oldPath, String newPath) throws IOException {
		java.io.File f = new java.io.File(oldPath);
		java.io.File nf = new java.io.File(newPath);

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

		//f.delete();

	}


	/* (non-Javadoc)
	 * @see org.deel.service.utils.FileSystemGateway#getFile(org.deel.domain.FileRevision)
	 */
	@Override
	public  FileInputStream getFile(String path) throws FileNotFoundException {
		String finalPath = storagePath + path; 
				
		// last.getUploadedBy().getUsername()
		//		+ last.getFsPath() + "." + last.getId();

		java.io.File fsFile = new java.io.File(finalPath);

		if (!fsFile.exists())
			throw new RuntimeException("DB/FS mismatch: file " + finalPath
					+ " doesn't exists");

		FileInputStream fIn = new FileInputStream(fsFile);


		return fIn;
	}

}
