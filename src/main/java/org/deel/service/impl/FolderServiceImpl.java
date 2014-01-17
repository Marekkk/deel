package org.deel.service.impl;

import org.deel.dao.FolderDao;
import org.deel.domain.Folder;
import org.deel.domain.User;
import org.deel.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;

public class FolderServiceImpl implements FolderService {

	@Autowired
	FolderDao folderDAO;
	
	@Override
	public Folder getFolder(String name, User user) {
		return folderDAO.getFolder(name, user);
	}

	@Override
	public void addFolder(Folder f) {
		folderDAO.insertFolder(f);
	}

}
