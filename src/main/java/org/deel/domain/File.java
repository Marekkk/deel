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
	
	@Column(name="fsPath")
	private String fsPath;

	@ManyToOne()
	@JoinColumn(name="user_id")
	private User owner;

	@Column(name="permission")
	private Permission permission;

	@OneToMany(fetch=FetchType.LAZY, mappedBy="file")
	private Set<FilePath> paths= new HashSet<FilePath>(0);

	@OneToMany(fetch=FetchType.LAZY, mappedBy="file")
	private Set<FileRevision> revision = new HashSet<FileRevision>(0);


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

	public String getFsPath() {
		return fsPath;
	}

	public void setFsPath(String fsPath) {
		this.fsPath = fsPath;
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

	@Override
	public String toString() {
		return "File [id=" + id + ", name=" + name + ", owner=" + owner + "]";
	}
	
}
