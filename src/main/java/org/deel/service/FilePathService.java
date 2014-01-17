package org.deel.service;

import java.util.List;

import org.deel.domain.FilePath;
import org.deel.domain.Folder;
import org.deel.domain.User;
import org.springframework.transaction.annotation.Transactional;

public interface FilePathService {
	
	@Transactional
	public List<FilePath> listOfPathFiles(User u, Folder f);
}
