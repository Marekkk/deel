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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="User")
public class User {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	private Long id;
	
	@Column(name="name")
	@NotEmpty
	private String name;
	
	@Column(name="surname")
	@NotEmpty
	private String surname;
	

	@Column(name="username", unique=true)
	@NotEmpty
	@Size(min=4, max=10)
	private String username;
	
	/* TODO make constrain with top level domain */
	@Email
	@NotEmpty
	@Column(name="email")
	private String email;
	
	@Column(name="password")
	private String password;
	
	@ManyToOne()
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	private Category category;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="user")
	private Set<FilePath> paths = new HashSet<FilePath>(0);
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="owner")
	private Set<File> files = new HashSet<File>(0);
	
	/*
	 * In a company can work multiple user, using CascadeType.ALL when we insert a new user with a new company 
	 * automatically the new company will be added to the DB
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	private Company company;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="User_Team", joinColumns = {@JoinColumn(name="user_id")},
								 inverseJoinColumns = {@JoinColumn(name="team_id")})
	private Set<Team> teams = new HashSet<Team>(0);
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="user")
	public Set<Folder> folders = new HashSet<Folder>();
	
	public Set<Team> getTeams() {
		return this.teams;
	}
	
	public void setTeams(Set<Team> teams) {
		this.teams = teams;
	}

	public Set<FilePath> getPaths() {
		return paths;
	}

	public void setPaths(Set<FilePath> paths) {
		this.paths = paths;
	}

	public void setFiles(Set<File> files) {
		this.files = files;
	}
	
	public Set<File> getFiles() {
		return this.files;
	}

	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public User(String email, String name, String surname, String username, String password, Category category) {
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.username = username;
		this.password = password;
		this.category = category;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
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

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Folder> getFolders() {
		return folders;
	}

	public void setFolders(Set<Folder> folders) {
		this.folders = folders;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + "]";
	}
	
}
