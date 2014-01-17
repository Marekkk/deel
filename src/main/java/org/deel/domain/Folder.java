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

import org.hibernate.Hibernate;
import org.hibernate.annotations.Cascade;
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

	@Column(name="name")
	private String name;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinTable(name="folderInFolder",
	joinColumns = {@JoinColumn(name="child")},
	inverseJoinColumns = {@JoinColumn(name="father")}
			)
	private Folder father;

	@ManyToOne()
	@JoinTable(name="user_folder",
	joinColumns = {@JoinColumn(name="folder_id")},
	inverseJoinColumns = {@JoinColumn(name="user_id")}
			)
	private User user;

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="folderInFolder",
	joinColumns = {@JoinColumn(name="Father")},
	inverseJoinColumns = {@JoinColumn(name="Child")}
			)
	private Set<Folder> inFolder = new HashSet<Folder>(0); // Other folder in folder

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="filepathsInFolder",
	joinColumns = {@JoinColumn(name="folder_id")},
	inverseJoinColumns = {@JoinColumn(name="filePath_id")}
			)
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

	public String getFSPath() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setFSPath(String string) {
		// TODO Auto-generated method stub
		
	}

}
