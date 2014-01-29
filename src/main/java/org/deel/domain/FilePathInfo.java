package org.deel.domain;

import java.util.Date;

import org.deel.domain.FilePath;

public class FilePathInfo {
	private Long id;
	private String name;
	private String uploadedBy;
	private Long size;
	private Date lastModified;
	private boolean isHidden;

	public FilePathInfo() {
	};

	public FilePathInfo(FilePath fp) {
		name = fp.getName();
		uploadedBy = fp.getFile().getRevisions().get(0).getUploadedBy().getUsername();
		size = fp.getFile().getRevisions().get(0).getSize();//dummy TODO
		lastModified = fp.getFile().getRevisions().get(0).getDate();
		isHidden = fp.isHidden();
		id = fp.getId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

}
