package org.deel.domain;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.deel.service.utils.FSUtils;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="FileRevision")
public class FileRevision {
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	private Long id;
	
	@Column(name="fsPath")
	private String fsPath;
	
	@Column(name="updated")
	private Date date;
	
	@ManyToOne()
	@JoinColumn(name="uploadedBy_id")
	private User uploadedBy;
	
	@ManyToOne()
	@JoinColumn(name="file_id")
	private File file;
	
	@Column(name="size")
	private Long size;
	
	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(User uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}


	public String getCompleteFsPath() {
		return getUploadedBy().getUsername()
				+ getFsPath() + "." + getId();
	}
	

}
