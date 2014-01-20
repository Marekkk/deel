package org.deel.domain;

import java.util.Set;

public class DirectoryListing {
	private Folder me;
	private Set<FilePath> filePaths;
	private Set<Folder> folders;
	public Folder getMe() {
		return me;
	}
	public void setMe(Folder me) {
		this.me = me;
	}
	public Set<FilePath> getFilePaths() {
		return filePaths;
	}
	public void setFilePaths(Set<FilePath> filePaths) {
		this.filePaths = filePaths;
	}
	public Set<Folder> getFolders() {
		return folders;
	}
	public void setFolders(Set<Folder> folders) {
		this.folders = folders;
	}
	
	
	
}
