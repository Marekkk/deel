package org.deel.domain;

import java.util.Set;

public class DirectoryListing {
	private FolderInfo me;
	private Set<FilePathInfo> filePaths;
	private Set<FolderInfo> folders;
	public FolderInfo getMe() {
		return me;
	}
	public void setMe(FolderInfo me) {
		this.me = me;
	}
	public Set<FilePathInfo> getFilePaths() {
		return filePaths;
	}
	public void setFilePaths(Set<FilePathInfo> filePaths) {
		this.filePaths = filePaths;
	}
	public Set<FolderInfo> getFolders() {
		return folders;
	}
	public void setFolders(Set<FolderInfo> folders) {
		this.folders = folders;
	}
	
	
	
}
