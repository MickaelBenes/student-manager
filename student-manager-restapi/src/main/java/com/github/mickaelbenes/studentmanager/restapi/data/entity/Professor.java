package com.github.mickaelbenes.studentmanager.restapi.data.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table( name = "professor" )
public class Professor extends Person implements Serializable {
	
	@Column( name = "username" )
	private String	username;

	@Column( name = "password" )
	@JsonIgnore
	private String	password;
	
	public Professor( String username, String password, String firstName, String lastName ) {
		super( firstName, lastName );
		
		this.username	= username;
		this.password	= password;
	}
	
	/**
	 * JPA only.
	 */
	public Professor() {}
	
	protected String getUsername() {
		return username;
	}
	
	protected void setUsername( String username ) {
		this.username = username;
	}
	
	protected String getPassword() {
		return password;
	}
	
	protected void setPassword( String password ) {
		this.password = password;
	}
	
}
