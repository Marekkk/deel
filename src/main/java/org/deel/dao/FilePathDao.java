package org.deel.dao;

import java.util.List;

import org.deel.domain.FilePath;
import org.deel.domain.Folder;
import org.deel.domain.User;

public interface FilePathDao {
	public void insertFilePath(FilePath fp);
	public void deleteFilePath(FilePath fp);
	public void updateFilePath(FilePath fp);
	public List<FilePath> listOfPathFiles(User user, Folder folder);
}
