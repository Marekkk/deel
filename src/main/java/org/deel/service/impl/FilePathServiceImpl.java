package org.deel.service.impl;

import java.util.List;

import org.deel.dao.FilePathDao;
import org.deel.domain.FilePath;
import org.deel.domain.Folder;
import org.deel.domain.User;
import org.deel.service.FilePathService;
import org.springframework.beans.factory.annotation.Autowired;

public class FilePathServiceImpl implements FilePathService{

	@Autowired
	FilePathDao filePathDAO;
	
	@Override
	public List<FilePath> listOfPathFiles(User u, Folder f) {
		return filePathDAO.listOfPathFiles(u, f); 
	}
	
}
