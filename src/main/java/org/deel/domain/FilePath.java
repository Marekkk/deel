package org.deel.domain;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="FilePath")
public class FilePath {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	private Long id;

	@Column(name="name")
	private String name;

	@ManyToOne(cascade={})
	@JoinColumn(name="user_id")
	private User user;

	@ManyToOne()
	@Cascade(CascadeType.SAVE_UPDATE)
	@JoinColumn(name="file_id")
	private File file;

	@ManyToOne()
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@JoinColumn(name="folder_id")
	private Folder folder;

	public FilePath() {}

	public FilePath(String name, User u, File file, Folder folder) {
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Folder getFolder() {
		return folder;
	}

	public void setFolder(Folder folder) {
		this.folder = folder;
	}

	@Override
	public String toString() {
		return "File name : " + this.name + " in the folder of : " + this.user.getUsername();
	}
}
