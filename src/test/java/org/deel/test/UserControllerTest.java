package org.deel.test;

import org.deel.controllers.UserController;
import org.deel.service.UserService;
import org.deel.validator.UserValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        MockitoAnnotations.initMocks(this);
        userController = new UserController();
        // Setup Spring test in standalone mode
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        this.userController.setUserValidator(new UserValidator());
        
        
    }
    @Test
    public void simpleTest() throws Exception {
    	this.mockMvc.perform(get("/user/new"))
    	.andExpect(status().isOk());
    }
    
}
