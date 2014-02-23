package org.deel.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
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
import org.deel.domain.FilePathInfo;
import org.deel.domain.FileRevision;
import org.deel.domain.Folder;
import org.deel.domain.FolderInfo;
import org.deel.domain.Team;
import org.deel.domain.User;
import org.deel.service.FileService;
import org.deel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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

	@RequestMapping("/file/revision/list")
	public @ResponseBody
	Map<String, Object> getRevisionList(@RequestParam Long id,
			Principal principal) {
		String username;
		if (principal != null)
			username = principal.getName();
		else 
			username = "Guest";
		Map<String, Object> jsonRet = new HashMap<String, Object>();
		User curr = userService.findUserByUsername(username);

		FilePath fp = new FilePath();
		fp.setId(id);
		List<FileRevision> fRevisions = fileService.getRevisionList(curr, fp);

		for (FileRevision fileRevision : fRevisions) {
			HashMap<String, Object> fRevisionJson = new HashMap<String, Object>();
			fRevisionJson.put("date", fileRevision.getDate());
			fRevisionJson.put("uploadedBy", fileRevision.getUploadedBy()
					.getUsername());
			fRevisionJson.put("name", fileRevision.getFile().getName());
			jsonRet.put(Long.toString(fileRevision.getId()), fRevisionJson);
		}

		return jsonRet;
	}

	@RequestMapping("/file/list")
	public @ResponseBody
	DirectoryListing getFilesListJSON(
			@RequestParam(value = "path", required = false) Long path,
			@RequestParam(value = "hidden", required = false) Boolean hidden,
			Principal principal) {
		
		
		/* If user isn't authenticated Guest user is used */
		String username;
		if (principal != null)
			username = principal.getName();
		else 
			username = "Guest";
		
		Map<String, Object> jsonRet = new HashMap<String, Object>();
		User curr = userService.findUserByUsername(username);

		Folder folder = new Folder();
		folder.setId(path);

		DirectoryListing list = fileService.listFolder(curr, folder);

		return list;
	}

	@RequestMapping("/file/download/**")
	public void downloadFile(@RequestParam Long id, Principal principal,
			HttpServletResponse response) {
		response.setContentType("application/octet-stream");

		String username;
		if (principal != null)
			username = principal.getName();
		else 
			username = "Guest";
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

	@RequestMapping("/file/revision/**")
	public void downloadFileRevision(@RequestParam Long id,
			@RequestParam Long revision, Principal principal,
			HttpServletResponse response) {
		response.setContentType("application/octet-stream");

		String username;
		if (principal != null)
			username = principal.getName();
		else 
			username = "Guest";
		User curr = userService.findUserByUsername(username);

		FilePath filePath = new FilePath();
		filePath.setId(id);

		FileRevision fileRevision = new FileRevision();
		fileRevision.setId(revision);

		FileInputStream is;
		try {
			is = fileService.getRevision(curr, filePath, fileRevision);
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
		e.printStackTrace();
		return json;
	}

	@RequestMapping(value = "/file/upload", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> fileUploadJSON(@ModelAttribute FileForm fileForm,
			BindingResult result, Principal principal, ModelMap model) {

		String username;
		if (principal != null)
			username = principal.getName();
		else 
			username = "Guest";
		User curr = userService.findUserByUsername(username);

		Folder folder = new Folder();
		folder.setId(fileForm.getPath());

		Map<String, Object> jsonReturn = new HashMap<String, Object>();

		List<MultipartFile> mFiles = fileForm.getFiles();
		Set<FilePathInfo> ret = new HashSet<FilePathInfo>();
		FilePathInfo fInfo;
		for (MultipartFile multipartFile : mFiles) {
			try {
				fInfo = fileService.uploadFile(curr,
						multipartFile.getOriginalFilename(), folder,
						multipartFile.getInputStream(),
						multipartFile.getSize());
				ret.add(fInfo);
				

			} catch (IOException e) {
				jsonReturn.put(multipartFile.getOriginalFilename(), "failed");
				jsonReturn.put(multipartFile.getOriginalFilename(), "error: "
						+ e.getMessage());

				e.printStackTrace();
			} catch (RuntimeException e) {
				jsonReturn.put(multipartFile.getOriginalFilename(), "failed");
				jsonReturn.put(multipartFile.getOriginalFilename(), "error: "
						+ e.getMessage());
				e.printStackTrace();
			}

		}
		jsonReturn.put("status", "success");
		jsonReturn.put("files", ret);

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
		String username;
		if (principal != null)
			username = principal.getName();
		else 
			username = "Guest";
		User u = userService.findUserByUsername(username);
		FolderInfo ret;
		try {
			ret = fileService.createNewFolder(u, folder, folderName);
		} catch (IOException e) {
			System.out.println("Error during creation of folder. \n" + e);
			result.put("status", "error");
			return result;
		}

		result.put("status", "success");
		result.put("folder", ret);

		return result;
	}

	@RequestMapping(value = "/folder/remove", method = RequestMethod.GET)
	public @ResponseBody
	Map<Long, String> removeDirectory(@RequestParam Long id, Principal principal) {
		Folder f = new Folder();
		f.setId(id);

		String username;
		if (principal != null)
			username = principal.getName();
		else 
			username = "Guest";
		User u = userService.findUserByUsername(username);

		fileService.deleteFolder(u, f);

		Map<Long, String> json = new HashMap<Long, String>();
		json.put(f.getId(), "success");

		return json;

	}

	@RequestMapping(value = "/file/share")
	public @ResponseBody
	Map<String, Object> shareFile(@RequestBody ShareFileMessage message,
			BindingResult result, Principal principal) {

		HashMap<String, Object> json = new HashMap<String, Object>();

		if (result.hasErrors()) {
			json.put("status", "failed");
			json.put("errors", result.getAllErrors());
			return json;
		}

		String username;
		if (principal != null)
			username = principal.getName();
		else 
			username = "Guest";
		User u = userService.findUserByUsername(username);

		FilePath fp = new FilePath();
		fp.setId(message.getFile());
		List<User> users = new LinkedList<User>();
		for (Long id : message.getUsers()) {
			User user = new User();
			user.setId(id);
			users.add(user);
		}
		
		List<Team> teams = new LinkedList<Team>();
		for (Long id : message.getTeams()) {
			Team team = new Team();
			team.setId(id);
			teams.add(team);
		}

		fileService.shareFile(u, fp, users, teams);

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
		String username;
		if (principal != null)
			username = principal.getName();
		else 
			username = "Guest";
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
	
	@RequestMapping(value = "/file/removeFromTrash", method = RequestMethod.GET)
	public @ResponseBody
	Map<Long, String> removeFileFromTrash(@RequestParam Long id, Principal principal) {

		Map<Long, String> result = new HashMap<Long, String>();
		System.out.println("We are going to remove filepath with id -> " + id);
		FilePath f = new FilePath();
		f.setId(id);
		String username;
		if (principal != null)
			username = principal.getName();
		else 
			username = "Guest";
		User u = userService.findUserByUsername(username);
		try {
			fileService.deleteFromTrash(f, u);
		} catch (Exception e) {
			System.out.println("Error during deleting. \n" + e);
			e.printStackTrace();
			result.put(id, "error");
			return result;
		}

		result.put(id, "success");

		return result;
	}
	
	@RequestMapping(value = "/folder/removeFromTrash", method = RequestMethod.GET)
	public @ResponseBody
	Map<Long, String> removeFolderFromTrash(@RequestParam Long id, Principal principal) {

		Map<Long, String> result = new HashMap<Long, String>();
		System.out.println("We are going to remove folder with id -> " + id);
		Folder f = new Folder();
		f.setId(id);
		String username;
		if (principal != null)
			username = principal.getName();
		else 
			username = "Guest";
		User u = userService.findUserByUsername(username);
		try {
			fileService.deleteFolderFromTrash(f, u);
		} catch (Exception e) {
			System.out.println("Error during deleting. \n" + e);
			e.printStackTrace();
			result.put(id, "error");
			return result;
		}

		result.put(id, "success");

		return result;
	}
}
