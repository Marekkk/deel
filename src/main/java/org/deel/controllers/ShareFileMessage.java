package org.deel.controllers;

import java.util.List;

import org.deel.domain.User;

public class ShareFileMessage {
	private Long file;
	private List<Long> users;
	private List<Long> teams;
	
	public List<Long> getTeams() {
		return teams;
	}
	public void setTeams(List<Long> teams) {
		this.teams = teams;
	}
	public Long getFile() {
		return file;
	}
	public void setFile(Long file) {
		this.file = file;
	}
	public List<Long> getUsers() {
		return users;
	}
	public void setUsers(List<Long> users) {
		this.users = users;
	}

}
