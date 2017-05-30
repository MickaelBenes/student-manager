package com.github.mickaelbenes.studentmanager.restapi;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.github.mickaelbenes.studentmanager.restapi.data.entity.Professor;
import com.github.mickaelbenes.studentmanager.restapi.data.entity.Skill;
import com.github.mickaelbenes.studentmanager.restapi.data.entity.Student;
import com.github.mickaelbenes.studentmanager.restapi.data.repository.ProfessorRepository;
import com.github.mickaelbenes.studentmanager.restapi.data.repository.SkillRepository;
import com.github.mickaelbenes.studentmanager.restapi.data.repository.StudentRepository;

@SpringBootApplication
public class App {
	
	public static void main( String[] args ) {
		SpringApplication.run( App.class, args );
	}
	
	@Bean
	CommandLineRunner init( ProfessorRepository profRepo, StudentRepository studentRepo, SkillRepository skillRepo ) {
		return evt -> Arrays.asList( "mbenes:Mickael:Benes,adutto:Anaelle:Dutto,apesenti:Anais:Pesenti,kpesenti:Kevin:Pesenti".split(",") )
				.forEach(p -> {
					List<String> infos	= Arrays.asList( p.split(":") );
					Professor prof		= profRepo.save( new Professor(infos.get(0), "password", infos.get(1), infos.get(2)) );
					Student student1	= studentRepo.save( new Student(prof, "Gabriel", "Faucon") );
					Student student2	= studentRepo.save( new Student(prof, "Jason", "Balas") );
					skillRepo.save( new Skill(student1, "Java", 3) );
					skillRepo.save( new Skill(student1, "PHP", 3) );
					skillRepo.save( new Skill(student2, "JavaScript", 5) );
					skillRepo.save( new Skill(student2, "HTML5", 4) );
				});
	}
	
}
