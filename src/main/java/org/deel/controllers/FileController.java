package org.deel.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.deel.dao.FolderDAO;
import org.deel.domain.DirectoryListing;
import org.deel.domain.FilePath;
import org.deel.domain.Folder;
import org.deel.domain.User;
import org.deel.service.FileService;
import org.deel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileController {
	private FileService fileService;
	private UserService userService;

	public UserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public FileService getFileService() {
		return fileService;
	}

	@Autowired
	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}
	

	
	@RequestMapping("/file/list")
	public @ResponseBody Map<String, Object> getFilesListJSON(@RequestParam(value = "path", required = false) Long path, Principal principal) {
		String username = principal.getName();
		Map<String, Object> jsonRet= new HashMap<String, Object>();
		User curr = userService.findUserByUsername(username);
		
		Folder folder = new Folder();
		folder.setId(path);
		
		DirectoryListing list = fileService.listFolder(curr, folder);
		

		Map<Long, String> fp = new HashMap<Long, String>();
		for (FilePath filePath : list.getFilePaths()) 
			fp.put(filePath.getId(), filePath.getName());
		
		Map<Long, String> dl = new HashMap<Long, String>();
		for (Folder f: list.getFolders()) 
			dl.put(f.getId(), f.getName());
		
		Map<String, Object> cd = new HashMap<String, Object>();
		cd.put("id", list.getMe().getId());
		cd.put("path", list.getMe().getFsPath());
		
		
		jsonRet.put("currentDir", cd);
		jsonRet.put("files", fp);
		jsonRet.put("directories", dl);
		
		return jsonRet;
	}
	
	@RequestMapping("/file/download")
	public void downloadFile(@RequestParam Long id, Principal principal, HttpServletResponse response) {
		response.setContentType("application/octet-stream");
		
		String username = principal.getName();
		User curr = userService.findUserByUsername(username);
		
		FilePath filePath= new FilePath();
		filePath.setId(id);
		FileInputStream is;
		try {
			is = fileService.getFile(curr, filePath);
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
			is.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return;
	}
	
	@RequestMapping(value = "/file/test", method= RequestMethod.GET)
	public String sfileUploadTest(){
		
	
		return "success";
	}

	@RequestMapping(value = "/file/test", method= RequestMethod.POST)
	public String fileUploadTest(@RequestParam("file") MultipartFile file){
		
		System.out.println("file name " + file.getName());
		return "success";
	}
	
	@ExceptionHandler(RuntimeException.class)
	public @ResponseBody Map<String,Object> exceptionHandler(Exception e) {
		Map<String, Object> json = new HashMap<String, Object>();
		RuntimeException re = (RuntimeException) e;
		
		json.put("error", e.getMessage());
		
		return json;
	}
	
	@RequestMapping(value = "/file/upload", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> fileUploadJSON(@ModelAttribute FileForm fileForm, 
			BindingResult result,
			Principal principal, ModelMap model) {

		
		String username = principal.getName();
		User curr = userService.findUserByUsername(username);
		
		Folder folder = new Folder();
		folder.setId(fileForm.getPath());
		
		Map<String, Object> jsonReturn = new HashMap<String, Object>();
		 
		List<MultipartFile> mFiles = fileForm.getFiles();
		
		for (MultipartFile multipartFile : mFiles) {
			try {
				fileService.uploadFile(curr, 
						multipartFile.getOriginalFilename(), 
						folder, 
						multipartFile.getInputStream());
				jsonReturn.put(multipartFile.getOriginalFilename(), 
						"success");
				
			} catch (IOException e) {
				jsonReturn.put(multipartFile.getOriginalFilename(), 
						"failed");
				jsonReturn.put(multipartFile.getOriginalFilename(), 
						"error: " + e.getMessage());
				
				e.printStackTrace();
			} catch (RuntimeException e) {
				jsonReturn.put(multipartFile.getOriginalFilename(), 
						"failed");
				jsonReturn.put(multipartFile.getOriginalFilename(), 
						"error: " + e.getMessage());
			}
			
		}

		/* TODO real message codes */
		return jsonReturn;
	}
}

