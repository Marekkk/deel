package org.deel.service;

import org.deel.domain.Folder;
import org.deel.domain.User;
import org.springframework.transaction.annotation.Transactional;

public interface FolderService {
	
	@Transactional
	public Folder getFolder(String name, User user);
}
