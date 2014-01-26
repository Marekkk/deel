package org.deel.domain;

public class FolderInfo {

	private FolderInfo father;
	private String name;
	private Long id;
	
	public FolderInfo(Folder f) {
		/* TODO maybe we not need it */
		father = (f.getFather() == null? null : new FolderInfo(f.getFather()));
		name = f.getName();
		id = f.getId();
	}

	public FolderInfo getFather() {
		return father;
	}

	public void setFather(FolderInfo father) {
		this.father = father;
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
