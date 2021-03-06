package org.deel.domain;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


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

import org.deel.service.utils.FSUtils;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="Folder")
public class Folder {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	private Long id;
	
	@Column(name="fsPath")
	private String fsPath;
	
	@Column(name="hidden", nullable = false)
	private boolean hidden;


	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	@Column(name="name")
	private String name;

	@ManyToOne
	@JoinTable(name="folderInFolder",
	joinColumns = {@JoinColumn(name="child")},
	inverseJoinColumns = {@JoinColumn(name="father")}
			)
	private Folder father;

	@ManyToOne()
	@JoinColumn(name="user_id")
	private User user;

	@OneToMany(fetch=FetchType.LAZY)
	@JoinTable(name="folderInFolder",
	joinColumns = {@JoinColumn(name="Father")},
	inverseJoinColumns = {@JoinColumn(name="Child")}
			)
	private Set<Folder> inFolder = new HashSet<Folder>(0); // Other folder in folder

	@OneToMany(fetch=FetchType.LAZY, mappedBy="folder")
	private Set<FilePath> filepaths = new HashSet<FilePath>(0);

	public Folder() {}

	public Folder(String name, Folder father, User user) {
		this.name = name;
		this.father = father;
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFsPath() {
		return fsPath;
	}

	public void setFsPath(String fsPath) {
		this.fsPath = fsPath;
	}

	public Folder getFather() {
		return father;
	}

	public void setFather(Folder father) {
		this.father = father;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<Folder> getInFolder() {
		return inFolder;
	}

	public void setInFolder(Set<Folder> inFolder) {
		this.inFolder = inFolder;
	}

	public Set<FilePath> getFilepaths() {
		return filepaths;
	}

	public void setFilepaths(Set<FilePath> filepaths) {
		this.filepaths = filepaths;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Folder [id=" + id + ", name=" + name + ", user=" + user + "]";
	}

	public String getCompleteFSPath() {
		return getUser().getUsername() + getFsPath();
	}

	public boolean existsFolder(String originalFilename) {
		for (Folder f : getInFolder())
			if (f.getName().equals(originalFilename))
				return true;
		return false;
	}

	public FilePath getFilePathByName(String originalFilename) {
		
		for(FilePath fp: getFilepaths())
			if (fp.getName().equals(originalFilename))
				return fp;
		
		return null;
	}
}
