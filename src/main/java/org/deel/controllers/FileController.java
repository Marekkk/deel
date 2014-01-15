package org.deel.controllers;

import java.io.IOException;
import java.util.List;

import org.deel.domain.User;
import org.deel.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileController {

	private FileService fileService;

	public FileService getFileService() {
		return fileService;
	}

	@Autowired
	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

	@RequestMapping(value = "/file/upload", method = RequestMethod.POST)
	public String fileUpload(FileForm files, ModelMap model) {
		/* TODO fix security.getPrincipal return our UserClass */
		User curr = new User();
		 
		List<MultipartFile> mFiles = files.getFiles();
		
		for (MultipartFile multipartFile : mFiles) {
			try {
				fileService.saveNewFile(curr, 
						multipartFile.getOriginalFilename(), 
						files.getPath(), 
						multipartFile.getBytes());
			} catch (IOException e) {
				// TODO gestione eccezioni
				e.printStackTrace();
			}
		}

		/* TODO real message codes */
		model.addAttribute("successCode", "file.uploaded");
		return "home";
	}
}

