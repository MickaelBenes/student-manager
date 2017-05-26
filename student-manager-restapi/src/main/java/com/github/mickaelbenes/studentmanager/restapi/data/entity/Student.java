package com.github.mickaelbenes.studentmanager.restapi.data.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: Student
 *
 */
@Entity
@Table( name = "student" )
public class Student extends Person implements Serializable {
	
	@OneToMany( mappedBy = "skill" )
	private Set<Skill> skills = new HashSet<Skill>();
	
	public Student( String firstName, String lastName ) {
		super( firstName, lastName );
	}

	/**
	 * JPA only.
	 */
	public Student() {}

}