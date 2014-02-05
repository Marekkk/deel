package org.deel.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Matchers.*;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.aspectj.weaver.reflect.Java14GenericSignatureInformationProvider;
import org.deel.service.FileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class FSAspectTest {

	@Autowired
	private FileService fileService;
	
	@Test
	public void undoSaveFileTest() throws IOException {
		try {
			fileService.uploadFile(null, "asd", null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		java.io.File f = new java.io.File("/home/garulf/test/asd");
		
		boolean wasDeleted = !f.exists();
		if (!wasDeleted)
			f.delete();
		
		Assert.assertTrue(wasDeleted);
		
		
	}
}
