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
import javax.xml.ws.RequestWrapper;

import org.apache.commons.io.IOUtils;
import org.deel.dao.FolderDAO;
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
import org.springframework.web.bind.annotation.RequestBody;
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
	
	@RequestMapping("/test")
	public @ResponseBody Map<String, Object> test(Principal principal, ModelMap map){
	
		Map<String,Object> m = new HashMap<String, Object>();
		m.put("test", "asd");
		
		String username = principal.getName();
		
		User curr = userService.findUserByUsername(username);
		
		m.put("principal", curr);
		return m;
	}
	
	@RequestMapping("/file/list")
	public @ResponseBody Map<String, Object> getFilesListJSON(@RequestParam(value = "path", required = false) Long path, Principal principal) {
		String username = principal.getName();
		Map<String, Object> jsonRet= new HashMap<String, Object>();
		User curr = userService.findUserByUsername(username);
		
		Folder folder = new Folder();
		folder.setId(path);
		
			
		folder = fileService.populateFolder(curr, folder);
		Set<FilePath> filePaths = fileService.getFilesInFolder(curr, folder);
		Set<Folder> folders = fileService.getFoldersInFolder(curr, folder);
		
		Map<Long, String> fp = new HashMap<Long, String>();
		for (FilePath filePath : filePaths) 
			fp.put(filePath.getId(), filePath.getName());
		
		Map<Long, String> dl = new HashMap<Long, String>();
		for (Folder f: folders) 
			dl.put(f.getId(), f.getName());
		
		Map<String, Object> cd = new HashMap<String, Object>();
		cd.put("id", folder.getId());
		cd.put("path", folder.getFsPath());
		
		
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
	
	@RequestMapping(value = "/file/addFolder", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> addFolder (@RequestParam long id, @RequestParam String folderName, Principal principal) {
		
		System.out.println(id + " " + folderName);
		Folder father = new Folder();
		father.setId(id);
		Folder folder = new Folder();
		folder.setFather(father);
		folder.setName(folderName);
		String username = principal.getName();
		User u = userService.findUserByUsername(username);
		try {
			fileService.createNewFolder(u, folder, folderName);
		} catch (IOException e) {
			System.out.println("Error during creation of folder. \n" + e);
		}
		
		
		return null;
	}
}

