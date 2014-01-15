package org.deel.test;

import java.beans.PropertyEditor;
import java.nio.channels.FileChannel.MapMode;
import java.util.List;
import java.util.Map;

import org.deel.controllers.UserController;
import org.deel.domain.User;
import org.deel.service.CustomUserDetailsService;
import org.deel.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import javax.servlet.*;

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
