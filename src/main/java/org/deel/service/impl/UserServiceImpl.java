package org.deel.service.impl;

import org.deel.dao.UserDao;
import org.deel.domain.User;
import org.deel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class UserServiceImpl implements UserService {

	private UserDao userDAO;

	public UserDao getUserDAO() {
		return userDAO;
	}

	@Autowired
	public void setUserDAO(UserDao userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	@Transactional
	public void addUser(User user) {
		userDAO.insertUser(user);
	}

	@Override
	@Transactional
	public User findUserByUsername(String username) {
		return userDAO.findUserByUsername(username);

	}

}
