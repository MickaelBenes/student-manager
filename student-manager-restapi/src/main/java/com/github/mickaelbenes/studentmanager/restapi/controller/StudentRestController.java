package com.github.mickaelbenes.studentmanager.restapi.controller;

import java.net.URI;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.mickaelbenes.studentmanager.restapi.data.entity.Student;
import com.github.mickaelbenes.studentmanager.restapi.data.repository.ProfessorRepository;
import com.github.mickaelbenes.studentmanager.restapi.data.repository.StudentRepository;
import com.github.mickaelbenes.studentmanager.restapi.data.resource.StudentResource;

@RestController
@RequestMapping( "/students" )
public class StudentRestController {
	
	private final ProfessorRepository	professorRepo;
	private final StudentRepository		studentRepo;
	
	@Autowired
	public StudentRestController( ProfessorRepository professorRepo, StudentRepository studentRepo ) {
		this.professorRepo	= professorRepo;
		this.studentRepo	= studentRepo;
	}
	
	@RequestMapping( method = RequestMethod.GET, value = "/{studentId}" )
	public StudentResource get( Principal principal, @PathVariable Long studentId ) {
		this.validateProfessor( principal );
		
		return new StudentResource( this.studentRepo.findOne(studentId) );
	}
	
	public ResponseEntity<?> add( Principal principal, @RequestBody Student input ) {
		this.validateProfessor( principal );
		
		return this.professorRepo
				.findByUsername( principal.getName() )
				.map(prof -> {
					Student student		= this.studentRepo.save( new Student(prof, input.getFirstName(), input.getLastName()) );
					Link forOneStudent	= new StudentResource( student ).getLink( Link.REL_SELF );
					
					return ResponseEntity.created( URI.create(forOneStudent.getHref()) ).build();
				})
				.orElse( ResponseEntity.noContent().build() );
	}
	
	private void validateProfessor( Principal principal ) {
		String profUsername	= principal.getName();
		
		this.professorRepo
			.findByUsername( profUsername )
			.orElseThrow( () -> new UsernameNotFoundException("User '" + profUsername + "' not found.") );
	}

}
