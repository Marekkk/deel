package org.deel.dao;

import org.deel.domain.User;

public interface UserDAO {
	public void insertUser(User u);
	public void updateUser(User u);
	public void deleteUser(User u);
	public User findUserByUsername(String Username);
}
