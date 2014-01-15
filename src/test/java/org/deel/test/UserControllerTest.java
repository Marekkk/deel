package org.deel.test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.deel.controllers.UserController;
import org.deel.domain.User;
import org.deel.service.CustomUserDetailsService;
import org.deel.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

public class UserControllerTest {

	@Mock
	private UserService userService;

	@InjectMocks
	private UserController userController;

	@Autowired
	private CustomUserDetailsService customDetailsService;

	private MockMvc mockMvc;

	@Before
	public void setup() {

		// Process mock annotations

		userController = new UserController();
		MockitoAnnotations.initMocks(this);
		// Setup Spring test in standalone mode
		this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

	}

	@Test
	public void simpleUserExistTest() throws Exception {
		this.mockMvc.perform(get("/user/new")).andExpect(status().isOk());

		when(userService.findUserByUsername(any(String.class))).thenReturn(new User());

		this.mockMvc.perform(
				post("/user/new").param("name", "Marek")
						.param("surname", "Mulner")
						.param("username", "diuk")
						.param("password", "asd")
						.param("email", "asd@asd"))
						.andExpect(
				model().hasErrors());

	}

	@Test
	public void validatorTest() throws Exception {
		this.mockMvc.perform(
				post("/user/new").param("name", "asd").param("surname", "asd")
						.param("password", "dsddsd")
						.param("email", "asd@asd.com")
						.param("username", "nick")).andExpect(
				model().hasNoErrors());

		this.mockMvc.perform(
				post("/user/new").param("name", "asd")
						.param("surname", "sdasda").param("password", "dsddsd")
						.param("email", "asd@").param("username", "nick"))
				.andExpect(model().hasErrors());

	}

	@Test
	public void passwordEncodingTest() throws Exception {
		User u = new User();
		u.setName("asd");
		u.setSurname("asd");
		u.setPassword("miau");
		u.setUsername("nick");

		BindingResult bindResultMock = mock(BindingResult.class);
		ModelMap modelMapMock = mock(ModelMap.class);

		userController.onSubmit(u, bindResultMock, modelMapMock);

		BCryptPasswordEncoder pwdEncode = new BCryptPasswordEncoder();

		Assert.assertTrue(pwdEncode.matches("miau", u.getPassword()));

	}

	@Test
	public void insertUserTest() throws Exception {

		this.mockMvc.perform(
				post("/user/new").param("name", "asd")
						.param("surname", "sdasda").param("password", "dsddsd")
						.param("email", "asd@asd").param("username", "nick"))
				.andExpect(model().hasNoErrors());

		verify(userService).addUser(any(User.class));

	}

	@Test
	public void insertDuplicateTest() throws Exception {
		when(userService.findUserByUsername("nick")).thenReturn(new User());

		this.mockMvc.perform(
				post("/user/new").param("name", "asd")
						.param("surname", "sdasda").param("password", "dsddsd")
						.param("email", "asd@asd").param("username", "nick"))
				.andExpect(model().hasErrors());

		verify(userService, never()).addUser(any(User.class));
	}

}
