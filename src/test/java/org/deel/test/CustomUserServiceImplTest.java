package org.deel.test;

import org.deel.domain.User;
import org.deel.service.CustomUserDetailsService;
import org.deel.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
@Transactional
public class CustomUserServiceImplTest {

	@Autowired
	private CustomUserDetailsService customDetailsService;
	
	@Autowired
	private UserService userService;
	
	@Before
	public void setup() {
		
	}
	
	@Test
	public void loadTest() throws Exception {
		User u = new User();
		
		u.setUsername("marco");
		u.setName("asd");
		u.setSurname("asd");
		u.setEmail("asd@asd");
		u.setPassword("asd");
		
		userService.addUser(u);
		
		UserDetails ud  = customDetailsService.loadUserByUsername("marco");
		
		Assert.assertEquals(ud.getUsername(), "marco");
		
	}
}
