package org.deel.service;

import org.deel.domain.User;

public interface UserService {
	public void addUser(User user);
	public User findUserByUsername(String username);
}
