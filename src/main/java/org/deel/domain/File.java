package org.deel.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name="File")
public class File {
	
	public enum Permission {
		Read, Write
	}

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@ManyToOne(cascade={})
	private User owner;
	
	@Column(name="permission")
	private Permission permission;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="file_filepath",
				joinColumns = {@JoinColumn(name="file_id")},
				inverseJoinColumns = {@JoinColumn(name="filePath_id")}
		      )
	private Set<FilePath> paths= new HashSet<FilePath>(0);
	
	public File() {}
	
	public File(String name, User u) {
		this.name = name;
		this.owner = u;
		this.permission = Permission.Read;
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

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}

	public Set<FilePath> getPaths() {
		return paths;
	}

	public void setPaths(Set<FilePath> paths) {
		this.paths = paths;
	}

	public void setFSPath(String string) {
		// TODO Auto-generated method stub
		
	}

	public String getFSPath() {
		// TODO Auto-generated method stub
		return null;
	}
}
