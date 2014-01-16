package org.deel.dao;

import org.deel.domain.Folder;

public interface FolderDao {
	public void insertFolder(Folder f);
	public void deleteFolder(Folder f);
	public void updateFolder(Folder f);
}
