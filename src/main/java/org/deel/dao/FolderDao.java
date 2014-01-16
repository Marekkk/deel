package org.deel.dao;

import org.deel.domain.Folder;
import org.deel.domain.User;

public interface FolderDao {
	public void insertFolder(Folder f);
	public void deleteFolder(Folder f);
	public void updateFolder(Folder f);
	public Folder getFolder(String name, User user);
}
