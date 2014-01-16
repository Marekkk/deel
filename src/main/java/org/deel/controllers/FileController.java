package org.deel.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.deel.domain.FilePath;
import org.deel.domain.User;
import org.deel.service.FileService;
import org.deel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
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
	public @ResponseBody List<FilePath> getFilesListJSON(@RequestParam String path, Principal principal) {
		String username = principal.getName();
		
		User curr = userService.findUserByUsername(username);
		
		return fileService.listFile(curr, path);
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
	
	@RequestMapping(value = "/file/upload", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> fileUploadJSON(@RequestParam("files") MultipartFile files, 
			BindingResult result,
			Principal principal, ModelMap model) {
		/* TODO fix security.getPrincipal return our UserClass */
//		//Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		String username;
//		if (principal instanceof UserDetails) {
//		  username = ((UserDetails)principal).getUsername();
//		} else {
//		  username = principal.toString();
//		}
		
		String username = principal.getName();
		
		User curr = userService.findUserByUsername(username);
		Map<String, Object> jsonReturn = new HashMap<String, Object>();
//		 
//		List<MultipartFile> mFiles = files.getFiles();
//		
//		for (MultipartFile multipartFile : mFiles) {
//			try {
//				fileService.saveNewFile(curr, 
//						multipartFile.getOriginalFilename(), 
//						files.getPath(), 
//						multipartFile.getBytes());
//				
//				jsonReturn.put(multipartFile.getOriginalFilename(), 
//						"success");
//				
//			} catch (IOException e) {
//				jsonReturn.put(multipartFile.getOriginalFilename(), 
//						"failed");
//				e.printStackTrace();
//			}
//		}

		/* TODO real message codes */
		return jsonReturn;
	}
}

