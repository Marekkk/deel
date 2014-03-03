package org.deel.domain;

public class UserInfo {
	private String username;
	private String name;
	private Long id;
	
	public UserInfo(User u) {
		this.username = u.getUsername();
		this.id = u.getId();
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
}	
