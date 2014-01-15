package org.deel.dao;

import org.deel.domain.User;

public interface UserDao {
	public void insertUser(User u);
	public User findUserByUsername(String Username);
}
