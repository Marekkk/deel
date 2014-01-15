package org.deel.service.impl;

import org.deel.dao.UserDao;
import org.deel.domain.User;
import org.deel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDAO;

	
	@Override
	@Transactional
	public void addUser(User user) {
		userDAO.insertUser(user);
	}

	@Override
	@Transactional
	public boolean userExist(User user) {
		// TODO Auto-generated method stub
		return false;
	}

}
