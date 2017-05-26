package com.github.mickaelbenes.studentmanager.restapi.data.entity;

import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: skill
 *
 */
@Entity
@Table( name = "skill" )
public class Skill implements Serializable {
	
	@Id
	@Column( name = "id" )
	@GeneratedValue
	private int id;

	@Column( name = "name" )
	private String name;

	public Skill() {
		super();
	}

	public int getId() {
		return this.id;
	}

	public void setId( int id ) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName( String name ) {
		this.name = name;
	}

}