package com.github.mickaelbenes.studentmanager.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.mickaelbenes.studentmanager.restapi.data.entity.Student;
import com.github.mickaelbenes.studentmanager.restapi.data.repository.StudentRepository;
import com.github.mickaelbenes.studentmanager.restapi.data.resource.StudentResource;

@RestController
@RequestMapping( "/students" )
public class StudentRestController {
	
	private final StudentRepository	studentRepo;
	
	@Autowired
	public StudentRestController( StudentRepository studentRepo ) {
		this.studentRepo	= studentRepo;
	}
	
	@RequestMapping( method = RequestMethod.GET, value = "/{studentId}" )
	public StudentResource get( @PathVariable Long studentId ) {
		return new StudentResource( this.studentRepo.findOne(studentId) );
	}
	
	public ResponseEntity<?> add( @RequestBody Student input ) {
		Student student	= this.studentRepo.save( new Student(input.getFirstName(), input.getLastName()) );
		
		return null;
	}

}
