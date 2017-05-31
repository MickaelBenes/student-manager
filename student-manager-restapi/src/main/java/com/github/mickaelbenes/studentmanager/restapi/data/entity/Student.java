package com.github.mickaelbenes.studentmanager.restapi.data.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity implementation class for Entity: Student
 *
 */
@Entity
@Table( name = "student" )
public class Student extends Person {
	
	@JsonIgnore
	@ManyToOne
	private Professor professor;
	
	@OneToMany( mappedBy = "student" )
	private List<Skill> skills = new ArrayList<>();
	
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

	public List<Skill> getSkills() {
		return skills;
	}

	public void addSkill( Skill skill ) {
		this.skills.add( skill );
	}
	
	public boolean hasSkills() {
		return !this.skills.isEmpty();
	}

}