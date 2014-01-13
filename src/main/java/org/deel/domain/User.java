package org.deel.domain;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Proxy;

@Entity
@Table(name="User")
@Proxy(lazy=false) // Using this for not use lazy load (with lazy load there are a wrong loading of collections!
public class User {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="surname")
	private String surname;
	
	@Column(name="username")
	private String username;
	
	@Column(name="password")
	private String password;
	
//	@ManyToOne(cascade=CascadeType.ALL)
//	private Category category;
//	
//	@OneToMany(cascade=CascadeType.ALL)
//	@JoinTable(name="user_file",
//				joinColumns = {@JoinColumn(name="user_id")},
//				inverseJoinColumns = {@JoinColumn(name="filePath_id")}
//		      )
//	private Set<FilePath> files = new HashSet<FilePath>(0);
//	
//	/*
//	 * In a company can work multiple user, using CascadeType.ALL when we insert a new user with a new company 
//	 * automatically the new company will be added to the DB
//	 */
//	@ManyToOne(cascade= {})
//	private Company company;
//	
//	@ManyToMany(cascade=CascadeType.ALL)
//	@JoinTable(name="User_Team", joinColumns = {@JoinColumn(name="user_id")},
//								 inverseJoinColumns = {@JoinColumn(name="team_id")})
//	private Set<Team> teams = new HashSet<Team>(0);
//	
//	public Set<Team> getTeams() {
//		return this.teams;
//	}
//	
//	public void setTeams(Set<Team> teams) {
//		this.teams = teams;
//	}
//
//	public Set<FilePath> getFiles() {
//		return files;
//	}
//
//	public void setFiles(Set<FilePath> files) {
//		this.files = files;
//	}
//
//	public User() {
//		// TODO Auto-generated constructor stub
//	}
//	
//	public User(String name, String surname, String username, String password, Company company, Set<Team> teams, Category category) {
//		this.name = name;
//		this.surname = surname;
//		this.username = username;
//		this.password = password;
//		this.company = company;
//		this.teams = teams;
//		this.category = category;
//	}
//	
//	public Category getCategory() {
//		return category;
//	}
//	
//	public void setCategory(Category category) {
//		this.category = category;
//	}
//	
//	public Company getCompany() {
//		return company;
//	}
//
//	public void setCompany(Company company) {
//		this.company = company;
//	}
//
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
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
//	
}
