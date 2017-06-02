package com.github.mickaelbenes.studentmanager.restapi;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

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
	
	// CORS
	@Bean
	FilterRegistrationBean corsFilter(
		@Value( "${tagit.origin:http://localhost:9000}") String origin ) {
		return new FilterRegistrationBean(new Filter() {
			public void doFilter( ServletRequest req, ServletResponse res, FilterChain chain ) throws IOException, ServletException {
				HttpServletRequest request		= (HttpServletRequest) req;
				HttpServletResponse response	= (HttpServletResponse) res;
				String method					= request.getMethod();
				
				// this origin value could just as easily have come from a database
				response.setHeader( "Access-Control-Allow-Origin", origin );
				response.setHeader( "Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE" );
				response.setHeader( "Access-Control-Max-Age", Long.toString(60 * 60) );
				response.setHeader( "Access-Control-Allow-Credentials", "true" );
				response.setHeader(
					"Access-Control-Allow-Headers",
					"Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization"
				);
				
				if ( "OPTIONS".equals(method) ) {
					response.setStatus( HttpStatus.OK.value() );
				}
				else {
					chain.doFilter( req, res );
				}
			}

			public void init(FilterConfig filterConfig) {}

			public void destroy() {}
		});
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
