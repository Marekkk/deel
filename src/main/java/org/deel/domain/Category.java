package org.deel.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Category")
public class Category {
	
	public enum type {
		Free, Premium, Full
	}
	
	@Id
	private type name;
	
	@Column(name="space")
	private int space;
	
	public Category() {
		// TODO Auto-generated constructor stub
	}
	
	public Category(type t, int space) {
		this.name = t;
		this.space = space;
	}

	public type getName() {
		return name;
	}

	public void setName(type name) {
		this.name = name;
	}

	public int getSpace() {
		return space;
	}

	public void setSpace(int space) {
		this.space = space;
	}
	
	
}
