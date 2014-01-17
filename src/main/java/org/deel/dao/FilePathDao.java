package org.deel.dao;

import org.deel.domain.FilePath;

public interface FilePathDao {
	public void insertFilePath(FilePath fp);
	public void deleteFilePath(FilePath fp);
	public void updateFilePath(FilePath fp);
}
