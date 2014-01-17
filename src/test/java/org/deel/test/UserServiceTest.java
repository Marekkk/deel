package org.deel.test;

import org.deel.domain.User;
import org.deel.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class UserServiceTest {
	@Autowired
	private UserService userService;
	
	
	@Before 
	public void setup() {
		
	}
	
	@Test
	public void loadExistingUserTest() throws Exception {
		User u = new User();
		
		u.setName("asd");
		u.setSurname("asd");
		u.setEmail("asd@asd");
		u.setPassword("asdasd");
		u.setUsername("asda");
		
		userService.registerNewUser(u);
		
		User u1 = userService.findUserByUsername("asda");
		
		
		Assert.assertEquals(u1.getUsername(), "asda");
	}

	@Test
	public void loadNotExistingUserTest() throws Exception {
		User u = userService.findUserByUsername("nilo");
		Assert.assertNull(u);
	}
	
}
