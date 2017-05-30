package com.github.mickaelbenes.studentmanager.restapi.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity implementation class for Entity: skill
 *
 */
@Entity
@Table( name = "skill" )
public class Skill {
	
	@Id
	@Column( name = "id" )
	@GeneratedValue
	private int id;
	
	@JsonIgnore
	@ManyToOne
	private Student student;

	@Column( name = "name" )
	private String name;
	
	@Column( name = "level" )
	private int level;
	
	public Skill( Student student, String name, int level ) {
		this.student	= student;
		this.name		= name;
		this.level		= level;
	}

	public Skill() {}

	public int getId() {
		return this.id;
	}

	public void setId( int id ) {
		this.id = id;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent( Student student ) {
		this.student = student;
	}

	public String getName() {
		return this.name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel( int level ) {
		this.level = level;
	}

}