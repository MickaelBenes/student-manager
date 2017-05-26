package com.github.mickaelbenes.studentmanager.restapi.data.resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceSupport;

import com.github.mickaelbenes.studentmanager.restapi.controller.StudentRestController;
import com.github.mickaelbenes.studentmanager.restapi.data.entity.Student;

public class StudentResource extends ResourceSupport {
	
	private final Student student;
	
	public StudentResource( Student student ) {
		this.student	= student;
		Long id			= this.student.getId();
		
		this.add( linkTo(
				methodOn( StudentRestController.class, id )
				.get( id )
			).withSelfRel()
		);
	}

}
