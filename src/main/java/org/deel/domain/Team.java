package org.deel.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="Team")
public class Team {
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="createdBy")
	private Long createdBy;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="User_Team", joinColumns = {@JoinColumn(name="team_id")},
								 inverseJoinColumns = {@JoinColumn(name="user_id")})
	private List<User> usersInTeam;

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

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public List<User> getUsersInTeam() {
		return usersInTeam;
	}

	public void setUsersInTeam(List<User> usersInTeam) {
		this.usersInTeam = usersInTeam;
	}
}
