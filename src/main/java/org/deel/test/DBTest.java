package org.deel.test;

import org.deel.domain.Category;
import org.deel.domain.Category.type;
import org.deel.domain.Company;
import org.deel.domain.File;
import org.deel.domain.FilePath;
import org.deel.domain.Folder;
import org.deel.domain.User;
import org.deel.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class DBTest {

	@Autowired
	private UserService userService;

	@Before
	public void init() throws Exception {
		
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void test1() {
		Company c = new Company("RPC");
		Category ca = new Category(type.Free, 20);
		User u = new User("pieror@gmail.com", "Pierpaolo", "Rametta", "rpiero", "abcd", ca);
		u.setCompany(c);
		Folder f = new Folder("photos", null, u);
		u.getFolders().add(f);
		FilePath fp = new FilePath("ciccio.png", u, new File("ciccio.png", u), f);
		u.getPaths().add(fp);
		userService.addUser(u);
		User user = userService.findUserByUsername("rpiero");
		System.out.println(user.getId());
	}
}
