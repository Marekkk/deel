package org.deel.test;

import org.deel.controllers.UserController;
import org.deel.domain.User;
import org.deel.service.UserService;
import org.deel.validator.UserValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import javax.servlet.*;


public class UserControllerTest {

	
	
	@Mock
	private UserService userService;
	
	@InjectMocks
	private UserController userController;
	
    private MockMvc mockMvc;
    
    @Before
    public void setup() {
 
    	
        // Process mock annotations
        
        userController = new UserController();
        MockitoAnnotations.initMocks(this);
        // Setup Spring test in standalone mode
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        this.userController.setUserValidator(new UserValidator());
        
        
    }
    @Test
    public void simpleUserExistTest() throws Exception {
    	this.mockMvc.perform(get("/user/new"))
    	.andExpect(status().isOk());
    	
    	when(userService.userExist(any(User.class))).thenReturn(true);
    	
    	this.mockMvc.perform(post("/user/new")
    			.param("name", "Marek")
    			.param("surname", "Mulner")
    			.param("username", "diuk")
    			.param("password", "asd"))
    			.andExpect(model().attributeExists("Error"));
    	
    }
    
    @Test
    public void validatorTest() throws Exception {
    	this.mockMvc.perform(post("/user/new")
    			.param("name", "asd")
    			.param("surname", "s")
    			.param("password", "dsd")
    			.param("username", "nick")
    			).andExpect(model().hasErrors());
    	
    	this.mockMvc.perform(post("/user/new")
    			.param("name", "asd")
    			.param("surname", "sdasda")
    			.param("password", "dsddsd")
    			.param("username", "nick")
    			).andExpect(model().hasNoErrors());
    }
    
}
