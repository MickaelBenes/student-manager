package com.github.mickaelbenes.studentmanager.restapi.data.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.mickaelbenes.studentmanager.restapi.data.entity.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long> {
	
	Collection<Skill> findByStudentLastName( String studentLastName );

}
