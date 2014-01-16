package org.deel.dao;

import org.deel.domain.File;

public interface FileDao {
	public void insertFile(File f);
	public void deleteFile(File f);
	public void updateFile(File f);
}
