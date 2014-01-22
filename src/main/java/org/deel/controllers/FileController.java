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

	@RequestMapping("/file/list")
	public @ResponseBody
	Map<String, Object> getFilesListJSON(
			@RequestParam(value = "path", required = false) Long path,
			Principal principal) {
		String username = principal.getName();
		Map<String, Object> jsonRet = new HashMap<String, Object>();
		User curr = userService.findUserByUsername(username);

		Folder folder = new Folder();
		folder.setId(path);

		DirectoryListing list = fileService.listFolder(curr, folder);

		Map<Long, String> fp = new HashMap<Long, String>();
		for (FilePath filePath : list.getFilePaths()) {
			/* TODO maybe can be optimized using a query with WHERE hidden=false */
			if (!filePath.isHidden())
				fp.put(filePath.getId(), filePath.getName());

		}

		Map<Long, String> dl = new HashMap<Long, String>();
		for (Folder f : list.getFolders()) {
			if (f.isHidden())
				continue;
			
			dl.put(f.getId(), f.getName());
		}

		Map<String, Object> cd = new HashMap<String, Object>();
		cd.put("id", list.getMe().getId());
		cd.put("path", list.getMe().getFsPath());

		jsonRet.put("currentDir", cd);
		jsonRet.put("files", fp);
		jsonRet.put("directories", dl);

		return jsonRet;
	}

	@RequestMapping("/file/download/**")
	public void downloadFile(@RequestParam Long id, Principal principal,
			HttpServletResponse response) {
		response.setContentType("application/octet-stream");

		String username = principal.getName();
		User curr = userService.findUserByUsername(username);

		FilePath filePath = new FilePath();
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

	@ExceptionHandler(RuntimeException.class)
	public @ResponseBody
	Map<String, Object> exceptionHandler(Exception e) {
		Map<String, Object> json = new HashMap<String, Object>();
		RuntimeException re = (RuntimeException) e;

		json.put("error", e.getMessage());
		json.put("trace", e.getStackTrace());
		return json;
	}

	@RequestMapping(value = "/file/upload", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> fileUploadJSON(@ModelAttribute FileForm fileForm,
			BindingResult result, Principal principal, ModelMap model) {

		String username = principal.getName();
		User curr = userService.findUserByUsername(username);

		Folder folder = new Folder();
		folder.setId(fileForm.getPath());

		Map<String, Object> jsonReturn = new HashMap<String, Object>();

		List<MultipartFile> mFiles = fileForm.getFiles();

		for (MultipartFile multipartFile : mFiles) {
			try {
				fileService.uploadFile(curr,
						multipartFile.getOriginalFilename(), folder,
						multipartFile.getInputStream());
				jsonReturn.put(multipartFile.getOriginalFilename(), "success");

			} catch (IOException e) {
				jsonReturn.put(multipartFile.getOriginalFilename(), "failed");
				jsonReturn.put(multipartFile.getOriginalFilename(), "error: "
						+ e.getMessage());

				e.printStackTrace();
			} catch (RuntimeException e) {
				jsonReturn.put(multipartFile.getOriginalFilename(), "failed");
				jsonReturn.put(multipartFile.getOriginalFilename(), "error: "
						+ e.getMessage());
			}

		}

		/* TODO real message codes */
		return jsonReturn;
	}

	@RequestMapping(value = "/file/addFolder", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> addFolder(@RequestParam Long id,
			@RequestParam String folderName, Principal principal) {

		Map<String, Object> result = new HashMap<String, Object>();
		System.out.println(id + " " + folderName);
		Folder folder = new Folder();
		folder.setId(id);
		String username = principal.getName();
		User u = userService.findUserByUsername(username);
		try {
			fileService.createNewFolder(u, folder, folderName);
		} catch (IOException e) {
			System.out.println("Error during creation of folder. \n" + e);
			result.put("Success", "error");
			return result;
		}

		result.put("Success", "success");

		return result;
	}

	@RequestMapping(value = "/folder/remove", method = RequestMethod.GET)
	public @ResponseBody
	Map<Long, String> removeDirectory(@RequestParam Long id, Principal principal) {
		Folder f = new Folder();
		f.setId(id);
		
		String username = principal.getName();
		User u = userService.findUserByUsername(username);


		fileService.deleteFolder(u, f);

		Map<Long, String> json = new HashMap<Long, String>();
		json.put(f.getId(), "success");
		
		return json;
		
	}

	@RequestMapping(value = "/file/share")
	public @ResponseBody Map<String, Object> shareFile(@RequestBody ShareFileMessage message, 
			BindingResult result,
			Principal principal) {
		
		HashMap<String, Object> json = new HashMap<String, Object>();
		
		if (result.hasErrors()) {
			json.put("status", "failed");
			json.put("errors", result.getAllErrors());
			return json;
		}
		
		String username = principal.getName();
		User u = userService.findUserByUsername(username);
		
		FilePath fp = new FilePath();
		fp.setId(message.getFile());
		List<User> users = new LinkedList<User>();
		for (Long id : message.getUsers()) {
			User user = new User();
			user.setId(id);
			users.add(user);
		}
		
		fileService.shareFile(u, fp, users);
		
		
		json.put("status", "sucess");
		
		
		return json;
		
	}
	@RequestMapping(value = "/file/remove", method = RequestMethod.GET)
	public @ResponseBody
	Map<Long, String> removeFile(@RequestParam Long id, Principal principal) {


		Map<Long, String> result = new HashMap<Long, String>();
		System.out.println("We are going to remove filepath with id -> " + id);
		FilePath f = new FilePath();
		f.setId(id);
		String username = principal.getName();
		User u = userService.findUserByUsername(username);
		try {
			fileService.deleteFile(u, f);
		} catch (IOException e) {
			System.out.println("Error during creation of folder. \n" + e);
			result.put(id, "error");
			return result;
		}

		result.put(id, "success");

		return result;
	}
}
