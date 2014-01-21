package org.deel.test;

import javax.transaction.Transactional;

import org.deel.dao.UserDAO;
import org.deel.domain.User;
import org.deel.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:fileUploadIntegrationTest.xml")
@WebAppConfiguration
@Transactional
public class userListTest {

	private UserDAO userDao;
	private UserService userService;

	public UserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserDAO getUserDao() {
		return userDao;
	}

	@Autowired
	public void setUserDao(UserDAO userDao) {
		this.userDao = userDao;
	}
	
	@Test
	public void userListTest() throws Exception {
		System.out.println(userDao.userList());
		User u = new User();
		u.setId((long)1);
		System.out.println(userService.listUser(u));
	}

	
	
}
