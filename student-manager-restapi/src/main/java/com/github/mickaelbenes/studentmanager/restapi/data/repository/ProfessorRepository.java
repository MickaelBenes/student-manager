package com.github.mickaelbenes.studentmanager.restapi.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.mickaelbenes.studentmanager.restapi.data.entity.Professor;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
	
	Optional<Professor> findByUsername( String username );

}
