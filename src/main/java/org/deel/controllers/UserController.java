package org.deel.controllers;

import javax.validation.Valid;

import org.deel.domain.User;
import org.deel.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {

	
	private UserService userService;
	

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	
	

	@RequestMapping(value = "/user/new", method = RequestMethod.GET)
	public String makeForm(ModelMap map) {
		User user = new User();
		map.addAttribute("user", user);
		return "newUser";
	}

	@RequestMapping(value = "/user/new", method = RequestMethod.POST)
	public String onSubmit(@Valid User user, BindingResult result,
			ModelMap map) {

		
		
		if (result.hasErrors()) {
			/* TODO show error messages */
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

		if (false) {
			userService.addUser(user);
		} else {
			map.addAttribute("Error", "Username already exist");
			return "newUser";
		}

		return "home";
	}

}
