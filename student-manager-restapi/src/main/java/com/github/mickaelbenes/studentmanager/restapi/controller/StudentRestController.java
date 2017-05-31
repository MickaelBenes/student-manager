package com.github.mickaelbenes.studentmanager.restapi.controller;

import java.net.URI;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.github.mickaelbenes.studentmanager.restapi.data.entity.Skill;
import com.github.mickaelbenes.studentmanager.restapi.data.entity.Student;
import com.github.mickaelbenes.studentmanager.restapi.data.repository.ProfessorRepository;
import com.github.mickaelbenes.studentmanager.restapi.data.repository.SkillRepository;
import com.github.mickaelbenes.studentmanager.restapi.data.repository.StudentRepository;
import com.github.mickaelbenes.studentmanager.restapi.exception.ProfessorNotFoundException;

@RestController
@RequestMapping( "/{profId}/students" )
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
	public Collection<Student> getStudents( @PathVariable String profId ) {
		this.validateProfessor( profId );
		
		return this.studentRepo.findByProfessorUsername( profId );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{studentId}" )
	public Student getStudent( @PathVariable String profId, @PathVariable Long studentId ) {
		this.validateProfessor( profId );
		
		return this.studentRepo.findOne( studentId );
	}
	
	@RequestMapping( method = RequestMethod.POST )
	ResponseEntity<?> addStudent( @PathVariable String profId, @RequestBody Student input ) {
		this.validateProfessor( profId );
		
		return this.professorRepo
				.findByUsername( profId )
				.map(prof -> {
					Student result	= this.studentRepo.save( new Student(prof, input.getFirstName(), input.getLastName()) );
					if ( input.hasSkills() ) {
						input.getSkills()
							.forEach(
								s -> this.skillRepo.save( new Skill(result, s.getName(), s.getLevel()) )
							);
					}
					
					URI location = ServletUriComponentsBuilder
							.fromCurrentRequest().path( "/{id}" )
							.buildAndExpand( result.getId() ).toUri();
					
					return ResponseEntity.created( location ).build();
				})
				.orElse( ResponseEntity.noContent().build() );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/{studentId}" )
	public ResponseEntity<?> addSkillToStudent( @PathVariable String profId, @PathVariable Long studentId, @RequestBody Skill input ) {
		this.validateProfessor( profId );
		Student student = this.studentRepo.findOne( studentId );
		
		return this.professorRepo
				.findByUsername( profId )
				.map(prof -> {
					this.skillRepo.save( new Skill(student, input.getName(), input.getLevel()) );
					
					URI location	= ServletUriComponentsBuilder
							.fromCurrentRequest()
							.build()
							.toUri();
					
					return ResponseEntity.created( location ).build();
				})
				.orElse( ResponseEntity.noContent().build() );
	}
	
//	private void validateProfessor( Principal principal ) {
//		String profUsername	= principal.getName();
//		
//		this.professorRepo
//			.findByUsername( profUsername )
//			.orElseThrow( () -> new UsernameNotFoundException("User '" + profUsername + "' not found.") );
//	}
	
	private void validateProfessor( String profId ) {
		this.professorRepo
			.findByUsername( profId )
			.orElseThrow( () -> new ProfessorNotFoundException(profId) );
	}

}
