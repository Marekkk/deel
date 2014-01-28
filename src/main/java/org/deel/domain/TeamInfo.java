package org.deel.domain;

public class TeamInfo {
	private Long id;
	private String name;
	
	public TeamInfo(Team t) {
		this.id = t.getId();
		this.name = t.getName();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
