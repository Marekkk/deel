package org.deel.domain;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;


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
	@NotEmpty
	@Pattern(regexp="^(?!\\.$)(?=.{1,40}$)([^\\\\:\\.*?<>\"|]*|\\.(?!\\.))*$")
	private String name;
	
	@ManyToOne()
	@JoinColumn(name="user_id")
	private User owner;

	@Column(name="permission")
	private Permission permission;

	@OneToMany(fetch=FetchType.LAZY, mappedBy="file")
	private Set<FilePath> paths= new HashSet<FilePath>(0);

	@OneToMany(fetch=FetchType.LAZY, mappedBy="file")
	@OrderBy("Id desc")
	private List<FileRevision> revisions = new LinkedList<FileRevision>();


	public List<FileRevision> getRevisions() {
		return revisions;
	}

	public void setRevisions(List<FileRevision> revisions) {
		this.revisions = revisions;
	}

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
