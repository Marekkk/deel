package org.deel.test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.security.Principal;

import javax.transaction.Transactional;

import org.deel.controllers.FileController;
import org.deel.dao.FolderDAO;
import org.deel.dao.UserDAO;
import org.deel.dao.impl.FolderDaoImpl;
import org.deel.dao.impl.UserDaoImpl;
import org.deel.domain.Folder;
import org.deel.domain.User;
import org.deel.service.impl.FileServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:fileUploadIntegrationTest.xml")
@WebAppConfiguration
@Transactional
public class FileUploadIntegrationTest {
	private MockMvc mockMvc;
	
	private FileController fileController;
	
	@Mock 
	private Principal principal;
	
	private MockMultipartFile multipartFile1;
	private MockMultipartFile multipartFile2;
	private FileInputStream fis;
	private FileInputStream fis1;
	public FileController getFileController() {
		return fileController;
	}

	@Autowired
	public void setFileController(FileController fileController) {
		this.fileController = fileController;
	}

	@Before
	public void setup() throws Exception{

		
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(fileController).build();
		when(principal.getName()).thenReturn("nick");

		fis = new FileInputStream("/home/garulf/info/esami/AE/code/random0");
		multipartFile1 = new MockMultipartFile("files[0]", "random0", "multipart/form-data", fis);

		fis1 = new FileInputStream("/home/garulf/info/esami/AE/code/random1");
		multipartFile2 = new MockMultipartFile("files[1]", "random1", "multipart/form-data", fis1);

		
	}
	
	@Test
	public void wiringTest() {
		assertNotNull(fileController.getFileService());
	}
	
	@Test 
	public void uploadFileTest() throws Exception {

		
		mockMvc.perform(
                fileUpload("/file/upload")
                .file(multipartFile1)
                .file(multipartFile2)
                .param("path", "/")
                .principal(principal)
                )
                .andExpect(status().isOk())
                .andDo(print());
		
//        File f1 = new File(fileController.getFileService().storagePath + "nick" + "/random0");
//        File f2 = new File(fileController.getFileService().storagePath + "nick" + "/random1");
//        assertTrue(f1.exists());
//        assertTrue(f2.exists());
//        
//        f1.delete();
//        f2.delete();
//        
        
	}
}
