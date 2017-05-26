package com.github.mickaelbenes.studentmanager.restapi.data.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.mickaelbenes.studentmanager.restapi.data.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
	
	Collection<Student> findByLastName( String lastName );

}
