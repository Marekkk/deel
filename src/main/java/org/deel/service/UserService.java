package org.deel.service;

import org.deel.domain.User;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {
	@Transactional
	public void addUser(User user);

	@Transactional
	public User findUserByUsername(String username);
}
