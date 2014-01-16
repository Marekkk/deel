package org.deel.test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.io.FileInputStream;
import java.security.Principal;
import java.util.LinkedList;
import java.util.List;

import org.deel.controllers.FileController;
import org.deel.domain.File;
import org.deel.domain.FilePath;
import org.deel.domain.Folder;
import org.deel.domain.User;
import org.deel.service.FileService;
import org.deel.service.UserService;
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
	
	@Mock Principal principal;
	
	
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
		when(principal.getName()).thenReturn("nick");
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
	                    .principal(principal)
	                    )
	                    .andExpect(status().isOk());
	            
	}
	
	@Test
	public void listFilesTest() throws Exception {
		User u = new User();
		u.setUsername("nick");
		when(userService.findUserByUsername("nick")).thenReturn(u);
		
		when(principal.getName()).thenReturn("nick");
		
		List<FilePath> files = new LinkedList<FilePath>();
		
		files.add(new FilePath("file0", u, new File("realName", u), new Folder("/home", null, u)));
		files.add(new FilePath("file1", u, new File("realName", u), new Folder("/home", null, u)));
		files.add(new FilePath("file2", u, new File("realName", u), new Folder("/home", null, u)));

		when(fileService.listFile(any(User.class), anyString())).thenReturn(files);
		
		mockMvc.perform(get("/file/list")
				.principal(principal)
				.param("path", "/home")
				).andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].path").value("file0"));
		
		
		
		
	}
	
	@Test
	public void principalMockTest() throws Exception {
		
		User u = new User();
		u.setName("nick");
		when(userService.findUserByUsername("nick")).thenReturn(u);
		

		when(principal.getName()).thenReturn("nick");
		
		 mockMvc.perform(get("/test")
				 .principal(principal)
                 )
                 .andDo(print())
                 .andExpect(status().isOk());
		 
		 verify(userService, times(1)).findUserByUsername(any(String.class));

	}
	
	@Test
	public void fileUploadAsd() throws Exception {

		
		when(userService.findUserByUsername("nick")).thenReturn(new User());
		
		List<SimpleGrantedAuthority> auth = new LinkedList<SimpleGrantedAuthority>();
		auth.add(new SimpleGrantedAuthority("ROLE_USER"));
		 mockMvc.perform(
                 fileUpload("/file/upload")
                 .file(multipartFile1)
                 .file(multipartFile2)
                 .param("path", "/my/path")
                 .principal(new PrincipalImpl("nick"))                 )
                 .andExpect(status().isOk())
                 .andDo(print())
                 .andExpect(jsonPath("$.random0").exists());

         

		verify(fileService, times(2)).saveNewFile(any(User.class), 
				anyString(), 
				anyString(), 
				any(byte[].class));
	
        
	}
}
