package com.github.mickaelbenes.studentmanager.restapi.data.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table( name = "professor" )
public class Professor extends Person {
	
	@Column( name = "username" )
	private String	username;

	@JsonIgnore
	@Column( name = "password" )
	private String	password;
	
	@OneToMany( mappedBy = "professor" )
	private List<Student> students = new ArrayList<>();
	
	public Professor( String username, String password, String firstName, String lastName ) {
		super( firstName, lastName );
		
		this.username	= username;
		this.password	= password;
	}
	
	/**
	 * JPA only.
	 */
	public Professor() {}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername( String username ) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword( String password ) {
		this.password = password;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents( List<Student> students ) {
		this.students = students;
	}
	
}
