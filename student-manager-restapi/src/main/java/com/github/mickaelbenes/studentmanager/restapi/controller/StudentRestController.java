package com.github.mickaelbenes.studentmanager.restapi.controller;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.mickaelbenes.studentmanager.restapi.data.entity.Skill;
import com.github.mickaelbenes.studentmanager.restapi.data.entity.Student;
import com.github.mickaelbenes.studentmanager.restapi.data.repository.ProfessorRepository;
import com.github.mickaelbenes.studentmanager.restapi.data.repository.SkillRepository;
import com.github.mickaelbenes.studentmanager.restapi.data.repository.StudentRepository;
import com.github.mickaelbenes.studentmanager.restapi.data.resource.StudentResource;
import com.github.mickaelbenes.studentmanager.restapi.exception.ProfessorNotFoundException;

@RestController
@RequestMapping( "/students" )
public class StudentRestController {
	
	private final ProfessorRepository	professorRepo;
	private final StudentRepository		studentRepo;
	private final SkillRepository		skillRepo;
	
	@Autowired
	public StudentRestController( ProfessorRepository professorRepo, StudentRepository studentRepo, SkillRepository skillRepo ) {
		this.professorRepo	= professorRepo;
		this.studentRepo	= studentRepo;
		this.skillRepo		= skillRepo;
	}
	
	@RequestMapping( method = RequestMethod.GET )
	public Resources<StudentResource> getStudents( Principal principal ) {
		this.validateProfessor( principal );
		
		List<StudentResource> studentResourceList = this.studentRepo.findByProfessorUsername( principal.getName() )
				.stream()
				.map( StudentResource::new )
				.collect( Collectors.toList() );
		
		return new Resources<>( studentResourceList );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{studentId}" )
	public StudentResource getStudent( Principal principal, @PathVariable Long studentId ) {
		this.validateProfessor( principal );
		
		return new StudentResource( this.studentRepo.findOne(studentId) );
	}
	
	@RequestMapping( method = RequestMethod.POST )
	ResponseEntity<?> addStudent( Principal principal, @RequestBody Student input ) {
		this.validateProfessor( principal );
		
		return this.professorRepo.findByUsername( principal.getName() )
				.map(prof -> {
					Student result	= this.studentRepo.save( new Student(prof, input.getFirstName(), input.getLastName()) );
					if ( input.hasSkills() ) {
						input.getSkills()
							.forEach(
								s -> this.skillRepo.save( new Skill(result, s.getName(), s.getLevel()) )
							);
					}
					
					Link forOneStudent = new StudentResource( result ).getLink( Link.REL_SELF );
					
					return ResponseEntity.created( URI.create(forOneStudent.getHref()) ).build();
				})
				.orElse( ResponseEntity.noContent().build() );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/{studentId}" )
	public ResponseEntity<?> addSkillToStudent( Principal principal, @PathVariable Long studentId, @RequestBody Skill input ) {
		this.validateProfessor( principal );
		Student student = this.studentRepo.findOne( studentId );
		
		return this.professorRepo
				.findByUsername( principal.getName() )
				.map(prof -> {
					this.skillRepo.save( new Skill(student, input.getName(), input.getLevel()) );
					
					Link forOneStudent = new StudentResource( student ).getLink( Link.REL_SELF );
					
					return ResponseEntity.created( URI.create(forOneStudent.getHref()) ).build();
				})
				.orElse( ResponseEntity.noContent().build() );
	}
	
	private void validateProfessor( Principal principal ) {
		String profUsername	= principal.getName();
		
		this.professorRepo
			.findByUsername( profUsername )
			.orElseThrow( () -> new ProfessorNotFoundException(profUsername) );
	}

}
