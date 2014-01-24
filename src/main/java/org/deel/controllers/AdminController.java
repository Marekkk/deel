package org.deel.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {

	@RequestMapping("/admin**")
	public String adminHome(ModelMap map, Principal principal) {
		
		return "admin";
	}
}
