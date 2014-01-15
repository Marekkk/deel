package org.deel.domain;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="FilePath")
public class FilePath {
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	private Long id;
	
	@Column(name="path")
	private String path;
	
	@ManyToOne(cascade={})
	private User user;
	
	@ManyToOne(cascade={})
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	private File file;
	
	@ManyToOne()
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	private Folder folder;
	
	public FilePath() {}
	
	public FilePath(String path, User u, File file, Folder folder) {
		this.path = path;
		this.user = u;
		this.file = file;
		this.folder = folder;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public String toString() {
		return "File name : " + this.path + " in the folder of : " + this.user.getUsername();
	}
}
