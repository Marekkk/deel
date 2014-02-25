package org.deel.controllers;

import java.security.Principal;

import org.deel.domain.User;
import org.deel.service.FileService;
import org.deel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class ControllerSuper {
	
	private UserService userService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public UserService getUserService() {
		return userService;
	}
	
	protected User getUser(Principal principal) {
		String username;
		if (principal != null)
			username = principal.getName();
		else 
			username = "Guest";
		
		return userService.findUserByUsername(username);
	}

}
