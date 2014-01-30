package org.deel.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.deel.domain.Company;
import org.deel.domain.Team;
import org.deel.domain.TeamInfo;
import org.deel.domain.User;
import org.deel.domain.UserInfo;
import org.deel.service.CompanyService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {


	private UserService userService;
	
	@Autowired
	private CompanyService companyService;


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
//		System.out.println(userList);
//		List<Long> usersId = new LinkedList<Long>();
//		List<String> usernames = new LinkedList<String>();

		List<UserInfo> usersInfo = new LinkedList<UserInfo>();
		
		for (User user : userList)  {
			usersInfo.add(new UserInfo(user));
//			usersId.add(user.getId());
//			usernames.add(user.getUsername());
		}

		json.put("status", "success");
		json.put("users", usersInfo);

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
		List<Company> companies = new LinkedList<Company>();
		companies = companyService.listCompany();
		map.addAttribute("companies", companies);
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
		System.out.println("Load page to change password");
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
	


	@RequestMapping("/team/create")
	public @ResponseBody Map<String, String> createTeam (@RequestBody teamCreateMessage message, Principal principal) {
		Map<String, String> json = new HashMap<String, String>();
		String name = message.getName();
		List<Long> usersId = message.getUsers();
		String username = principal.getName();
		User user = userService.findUserByUsername(username);
		Team t = userService.findTeamByName(name);
		if (t != null) {
			json.put("status", "Team with this name already exists.");
			return json;
		}
		if (name == "") {
			json.put("status", "Please insert a name.");
			return json;
		}
		
		List<User> users = new LinkedList<User>();
		for (Long id : usersId) {
			User temp = new User();
			temp.setId(id);
			users.add(temp);
		}
		
		Team team = new Team();
		team.setCreatedBy(user.getId());
		team.setName(name);
		team.setUsersInTeam(users);
		
		userService.addTeam(team);
		
		json.put("status", "Team created!");
		
		return json;
	}
	
	@RequestMapping("/team/list")
	public @ResponseBody Map<String, Object> teamList (Principal principal) {
		Map<String, Object> json = new HashMap<String, Object>();

		List<Team> teamList = userService.getTeams();
//		System.out.println(teamList);
//		List<Long> teamsId = new LinkedList<Long>();
//		List<String> teamsName = new LinkedList<String>();
		
		List<TeamInfo> teams = new LinkedList<TeamInfo>();

		for (Team team : teamList)  {
			teams.add(new TeamInfo(team));
//			teamsId.add(team.getId());
//			teamsName.add(team.getName());
		}

		json.put("status", "success");
		json.put("teams", teams);

		return json;
	}
}
