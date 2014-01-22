package org.deel.controllers;

import java.util.List;

import org.deel.domain.User;

public class ShareFileMessage {
	private Long file;
	public Long getFile() {
		return file;
	}
	public void setFile(Long file) {
		this.file = file;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	private List<User> users;
}
