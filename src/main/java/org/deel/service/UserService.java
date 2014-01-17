package org.deel.service;

import java.io.IOException;

import org.deel.domain.User;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {
	@Transactional
	public void registerNewUser(User user) throws IOException;

	@Transactional
	public User findUserByUsername(String username);
}
