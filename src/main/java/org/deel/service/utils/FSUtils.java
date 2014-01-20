package org.deel.service.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.management.RuntimeErrorException;

import org.apache.commons.io.IOUtils;
import org.deel.domain.File;
import org.deel.domain.Folder;



public class FSUtils {

	private static String storagePath = System.getProperty("user.home") + "/storage/";
	
	public static String getStoragePath() {
		return storagePath;
	}

	public static void setStoragePath(String storagePath) {
		FSUtils.storagePath = storagePath;
	}

	public static void mkdir(Folder f) throws IOException {

		java.io.File dir = new java.io.File(storagePath
				+ f.getUser().getUsername() + f.getFsPath());
		if (!dir.mkdir())
			throw new RuntimeErrorException(new Error("directory.notcreated"),
					"Can't make dir" + dir.getAbsolutePath());
	}

	public static void deleteFile(File f) {
		java.io.File file = new java.io.File(storagePath + f.getOwner().getUsername() + f.getFsPath());
		
		if(!file.exists() || file.isDirectory())
			throw new RuntimeException("DB/FS mismatch file " + file.getAbsolutePath()  + "doesnt exists");
		
		if(!file.delete())
			throw new RuntimeException("Can't delete file " + f.getFsPath());
		
	}

	public static void mv(String oldPath, String newPath) throws IOException {
		java.io.File f = new java.io.File(storagePath + oldPath);
		java.io.File nf = new java.io.File(storagePath + newPath);
		
		if(!f.exists())
			throw new RuntimeException("Error mv " + oldPath + " does not exists!");
		
		if (f.isDirectory())
			throw new RuntimeException("Error mv " + oldPath + " is a directory!");
			
		FileInputStream fIn = new FileInputStream(f);
		FileOutputStream fOut = new FileOutputStream(nf);
		IOUtils.copy(fIn, fOut);
		fIn.close();
		fOut.close();
		
		f.delete();
		
	}

}