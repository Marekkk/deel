package org.deel.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.deel.domain.User;
import org.deel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

	
	private UserService userService;
	

	public UserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping(value = "/user/new.json", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> onSubmitJson(@Valid User user, BindingResult result,
			ModelMap map) {
		HashMap<String, Object> json = new HashMap<String, Object>();
		
		/* checking password */
		if (user.getPassword() == null && user.getPassword().length() < 5) {
			result.rejectValue("password",
	                 "password.tooShort",
	                 "password should be at least 5 chars");
		}
		
		if (result.hasErrors()) {
			HashMap<String, String> errors = new HashMap<String, String>();
			for (ObjectError e : result.getAllErrors()) 
				errors.put(e.getCode(), e.toString());
			json.put("status", "failed");
			json.put("errors", errors);
			
		}
		
		/* TODO use dependency injection to inject pwdEncoder */
		BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();	
		user.setPassword(pwdEncoder.encode(user.getPassword()));

		try {
			userService.registerNewUser(user);
		} catch (RuntimeException e)	{
			result.addError(new ObjectError("user", e.getMessage()));
			System.out.println("RunTimeException while registering User " + e.getMessage());
			e.printStackTrace();
			json.put("status", "failed");
			json.put("errors", e.getMessage());
			return json;
		} catch (IOException e) {
			result.addError(new ObjectError("user", e.getMessage()));
			e.printStackTrace();
			json.put("status", "failed");
			json.put("errors", e.getMessage());
			return json;
		}
		
		json.put("status", "success");
		return json;
	}

	@RequestMapping(value="/user/list")
	public @ResponseBody Map<String, Object> userList(Principal principal) 
	{
		Map<String, Object> json = new HashMap<String, Object>();
		String username = principal.getName();
		User curr = userService.findUserByUsername(username);
		
		List<User> userList = userService.listUser(curr);
		List<Long> usersId = new LinkedList<Long>();
		List<String> usernames = new LinkedList<String>();
		
		for (User user : userList)  {
			usersId.add(user.getId());
			usernames.add(user.getUsername());
			//json.put(user.getId(), user.getUsername());
		}
		
		json.put("id", usersId);
		json.put("Username", usernames);
		
		return json;
	}
	
	@RequestMapping(value="/user/selectUsers")
	public String selectUsers() {
		return "userListSelectable";
	}

	@RequestMapping(value = "/user/new", method = RequestMethod.GET)
	public String makeForm(ModelMap map) {
		User user = new User();
		user.setPassword("");
		map.addAttribute("user", user);
		return "newUser";
	}

	@RequestMapping(value = "/user/new", method = RequestMethod.POST)
	public String onSubmit(@Valid User user, BindingResult result,
			ModelMap map) {

		
		
		if (result.hasErrors()) {
			LinkedList<String> errors = new LinkedList<String>();
			
			for (ObjectError error : result.getAllErrors()) {
				
				if (error.getDefaultMessage() != null)
					errors.add(error.getDefaultMessage());
				else
					if (error.getCodes() != null)
						errors.add(error.getCode());
			}
			
			map.addAttribute("errors", errors);
			return "newUser";
		}
		
		/* checking password */
		if (user.getPassword() == null && user.getPassword().length() < 5) {
			result.rejectValue("password",
	                 "password.tooShort",
	                 "password should be at least 5 chars");
		}
		
		/* TODO use dependency injection to inject pwdEncoder */
		BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();	
		user.setPassword(pwdEncoder.encode(user.getPassword()));

		try {
			userService.registerNewUser(user);
		} catch (RuntimeException e)	{
			result.addError(new ObjectError("user", e.getMessage()));
			System.out.println("RunTimeException while registering User " + e.getMessage());
			e.printStackTrace();
			map.addAttribute("errors", e.getMessage());
			user.setPassword("");
			return "newUser";
		} catch (IOException e) {
			result.addError(new ObjectError("user", e.getMessage()));
			e.printStackTrace();
			user.setPassword("");
			map.addAttribute("errors", e.getMessage());
			return "newUser";
		}

		return "redirect:/home";
	}
	
	@RequestMapping("/user/settingsProfile")
	public String goToUpdate (ModelMap map, Principal principal) {
		String username = principal.getName();
		map.addAttribute("user", username);
		return "profile";
	}
	
	@RequestMapping("/user/updatePsw")
	public @ResponseBody Map<String, String> updateProfile (@RequestParam String old, @RequestParam String password, Principal principal, ModelMap map) {
		System.out.println(old);
		Map<String, String> result = new HashMap<String, String>();
		String username = principal.getName();
		User u = userService.findUserByUsername(username);
		BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
		String oldpsw = u.getPassword();
		if (!pwdEncoder.matches(old, oldpsw)){
			System.out.println("Error : mismatch old password!");
			String error = "Error";
			result.put("status", error);
			return result;
		}
		u.setPassword(pwdEncoder.encode(password));
		userService.updateUser(u);
		String noError = "Password has been changed!";
		result.put("status", noError);
		return result;
	}

}
