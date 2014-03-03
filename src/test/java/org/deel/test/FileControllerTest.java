package org.deel.test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Principal;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import sun.security.acl.PrincipalImpl;

public class FileControllerTest {
	private MockMvc mockMvc;

	@Mock
	private FileService fileService;
	
	@Mock 
	private UserService userService;
	
	@Mock 
	private Principal principal;
	
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
		
		
		Folder f = new Folder();
		f.setId((long)1);
		f.setName("/");
		f.setFsPath("/");
		f.setUser(u);
		
		Set<FilePath> files = new HashSet<FilePath>();
		FilePath fp1 = new FilePath();
		FilePath fp2 = new FilePath();
		
		fp1.setId((long)1);
		fp2.setId((long)2);
		
		fp1.setFile(null);
		fp2.setFile(null);
		
		fp1.setName("ciaocia");
		fp2.setName("asdasd");
		
		
		files.add(fp1);
		files.add(fp2);
		
		when(principal.getName()).thenReturn("nick");
		when(userService.findUserByUsername("nick")).thenReturn(u);
			
		mockMvc.perform(get("/file/list")
				.principal(principal)
				.param("path", "1")
				).andDo(print());
		
	}
	
	@Test 
	public void exceptionTest() throws Exception {
		doThrow(new RuntimeException("asd")).when(fileService).uploadFile(any(User.class), 
				anyString(), 
				any(Folder.class),
				any(InputStream.class),any(Long.class));
		

        mockMvc.perform(
                fileUpload("/file/upload")
                .file(multipartFile1)
                .file(multipartFile2)
                .param("path", "/my/path")
                .principal(principal)
                )
                .andExpect(status().isOk())
                .andDo(print());
		
	}
	
	@Test
	public void principalMockTest() throws Exception {
		
		User u = new User();
		
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
	public void downloadTest() throws Exception {
		User u = new User();
		FilePath fp = new FilePath();
		
		java.io.File f = new java.io.File("/home/garulf/storage/nick/sheep_wolves.bk");
		FileInputStream fs = new FileInputStream(f);
		
		when(userService.findUserByUsername("nick")).thenReturn(u);
		when(principal.getName()).thenReturn("nick");
		when(fileService.getFile(any(User.class), any(FilePath.class))).thenReturn(fs);
		
		mockMvc.perform(get("/file/download")
				 .principal(principal)
				 .param("id", "2")
                 )
                 .andDo(print())
                 .andExpect(status().isOk());
		 verify(fileService).getFile(any(User.class), any(FilePath.class));

	}
	
	@Test
	public void downloadDifferentOwnerTest() throws Exception {
		User u = new User();
		FilePath fp = new FilePath();
		
		java.io.File f = new java.io.File("/home/garulf/storage/nick/sheep_wolves.bk");
		FileInputStream fs = new FileInputStream(f);
		
		when(userService.findUserByUsername("nick")).thenReturn(u);
		when(principal.getName()).thenReturn("nick");
		when(fileService.getFile(any(User.class), any(FilePath.class))).thenThrow(new RuntimeException());
		
		mockMvc.perform(get("/file/download")
				 .principal(principal)
				 .param("id", "2")
                 )
                 .andDo(print())
                 .andExpect(status().isOk());
		 verify(fileService).getFile(any(User.class), any(FilePath.class));

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

         

		verify(fileService, times(2)).uploadFile(any(User.class), 
				anyString(), 
				any(Folder.class), 
				any(InputStream.class)
				,any(Long.class));
	
        
	}
}
