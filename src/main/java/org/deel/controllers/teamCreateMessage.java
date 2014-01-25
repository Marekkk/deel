package org.deel.controllers;

import java.util.List;

public class teamCreateMessage {
	private List<Long> users;
	private String name;
	public List<Long> getUsers() {
		return users;
	}
	public void setUsers(List<Long> users) {
		this.users = users;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
