package org.deel.test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;

import org.deel.controllers.FileController;
import org.deel.domain.User;
import org.deel.service.FileService;
import org.deel.service.UserService;
import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import sun.security.acl.PrincipalImpl;

public class UploadControllerTest {
	private MockMvc mockMvc;
	
	@Mock
	private FileService fileService;
	
	@Mock 
	private UserService userService;
	
	@InjectMocks
	private FileController fileController;
	
	private MockMultipartFile multipartFile1;
	private MockMultipartFile multipartFile2;
	private FileInputStream fis;

	@Before
	public void setup() throws Exception {
		fileController = new FileController();
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(fileController).build();
		
        fis = new FileInputStream("/home/garulf/info/esami/AE/code/random0");
        multipartFile1 = new MockMultipartFile("files[0]", "random0", "multipart/form-data", fis);
        multipartFile1.getOriginalFilename();
        fis = new FileInputStream("/home/garulf/info/esami/AE/code/random1");
        multipartFile2 = new MockMultipartFile("files[1]", "random1", "multipart/form-data", fis);


	}
	
	@Test
	public void statusIsOkUpload() throws Exception {

	         

	            mockMvc.perform(
	                    fileUpload("/file/upload")
	                    .file(multipartFile1)
	                    .file(multipartFile2)
	                    .param("path", "/my/path")
	                    )
	                    .andExpect(status().isOk());
	            
	}
	
	@Test
	public void fileUploadAsd() throws Exception {

		User u = new User();
		u.setName("nick");
		when(userService.findUserByUsername("nick")).thenReturn(u);
		
		List<SimpleGrantedAuthority> auth = new LinkedList<SimpleGrantedAuthority>();
		auth.add(new SimpleGrantedAuthority("ROLE_USER"));
		 mockMvc.perform(
                 fileUpload("/file/upload")
                 .file(multipartFile1)
                 .file(multipartFile2)
                 .param("path", "/my/path")
                 .principal(new PrincipalImpl("nick"))                 )
                 .andExpect(status().isOk())
                 .andExpect(model().attribute("random0", "success"));
         

		verify(fileService, times(2)).saveNewFile(any(User.class), 
				anyString(), 
				anyString(), 
				any(byte[].class));
	
		
        
	}
}
