package com.github.mickaelbenes.studentmanager.restapi.data.resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import com.github.mickaelbenes.studentmanager.restapi.controller.StudentRestController;
import com.github.mickaelbenes.studentmanager.restapi.data.entity.Skill;
import com.github.mickaelbenes.studentmanager.restapi.data.entity.Student;

public class StudentResource extends ResourceSupport {
	
	private final Student student;
	
	public StudentResource( Student student ) {
		this.student	= student;
		String username	= this.student.getProfessor().getUsername();
		
		this.add( linkTo(StudentRestController.class, username).withRel("students") );
		this.add( linkTo(
				methodOn( StudentRestController.class, username )
				.getStudent( username, this.student.getId() )
			).withSelfRel()
		);
	}
	
	public Student getStudent() {
		return this.student;
	}
	
	public List<Skill> getSkills() {
		return this.student.getSkills();
	}
	
}
