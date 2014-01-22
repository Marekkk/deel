package org.deel.dao;


import org.deel.domain.FileRevision;

public interface FileRevisionDAO {
	public Long insert(FileRevision fr);
	public void delete(FileRevision fp);
	public void update(FileRevision fp);
	public FileRevision get(FileRevision fp);
}
