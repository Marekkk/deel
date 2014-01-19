package org.deel.service.utils;

import java.io.IOException;

import javax.management.RuntimeErrorException;

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

}
