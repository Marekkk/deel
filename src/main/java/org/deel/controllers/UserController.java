package org.deel.controllers;

import java.util.List;

import javax.servlet.jsp.jstl.core.Config;

import org.deel.domain.User;
import org.deel.service.UserService;
import org.deel.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

	private UserValidator userValidator;
	private UserService userService;

	public UserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserValidator getUserValidator() {
		return userValidator;
	}

	@Autowired
	public void setUserValidator(UserValidator userValidator) {
		this.userValidator = userValidator;
	}

	@RequestMapping(value = "/user/new", method = RequestMethod.GET)
	public String makeForm(ModelMap map) {
		User user = new User();
		map.addAttribute("user", user);
		return "newUser";
	}

	@RequestMapping(value = "/user/new", method = RequestMethod.POST)
	public String onSubmit(@ModelAttribute("user") User user, Errors errors,
			ModelMap map) {

		userValidator.validate(user, errors);
		
		if (errors.hasErrors()) {

		
			map.addAttribute("Error", "You insert incorrect values");
			return "newUser";
		}

		if (!userService.userExist(user)) {
			userService.addUser(user);
		} else {
			map.addAttribute("Error", "Username already exist");
			return "newUser";
		}

		return "home";
	}

}
