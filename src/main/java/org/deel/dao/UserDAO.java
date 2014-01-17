package org.deel.dao;

import org.deel.domain.User;


public interface UserDAO {
	public Long insertUser(User u);
	public void updateUser(User u);
	public void deleteUser(User u);
	public User findUserByUsername(String Username);
}