package com.github.mickaelbenes.studentmanager.restapi.data.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Person {
	
	@Id
	@Column( name = "id" )
	@GeneratedValue
	protected Long		id;
	
	@Column( name = "firstName" )
	protected String	firstName;
	
	@Column( name = "lastName" )
	protected String	lastName;
	
	public Person( String firstName, String lastName ) {
		this.firstName	= firstName;
		this.lastName	= lastName;
	}
	
	public Person() {}

	public Long getId() {
		return this.id;
	}

	public void setId( Long id ) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName( String firstName ) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName( String lastName ) {
		this.lastName = lastName;
	}

}
