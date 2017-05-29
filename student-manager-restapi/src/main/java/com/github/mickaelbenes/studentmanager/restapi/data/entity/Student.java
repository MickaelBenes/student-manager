package com.github.mickaelbenes.studentmanager.restapi.data.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity implementation class for Entity: Student
 *
 */
@Entity
@Table( name = "student" )
public class Student extends Person implements Serializable {
	
	@JsonIgnore
	@OneToOne( mappedBy = "professor" )
	private Professor professor;
	
	@OneToMany( mappedBy = "skill" )
	private Set<Skill> skills = new HashSet<Skill>();
	
	public Student( Professor professor, String firstName, String lastName ) {
		super( firstName, lastName );
		
		this.professor = professor;
	}

	/**
	 * JPA only.
	 */
	public Student() {}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor( Professor professor ) {
		this.professor = professor;
	}

	public Set<Skill> getSkills() {
		return skills;
	}

	public void setSkills( Set<Skill> skills ) {
		this.skills = skills;
	}

}