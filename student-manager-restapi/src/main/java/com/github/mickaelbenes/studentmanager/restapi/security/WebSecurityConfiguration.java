package com.github.mickaelbenes.studentmanager.restapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.github.mickaelbenes.studentmanager.restapi.data.repository.ProfessorRepository;
import com.github.mickaelbenes.studentmanager.restapi.exception.ProfessorNotFoundException;

@Configuration
public class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
	
	@Autowired
	private ProfessorRepository profRepo;
	
	@Override
	public void init( AuthenticationManagerBuilder auth ) throws Exception {
		auth.userDetailsService( this.userDetailsService() );
	}
	
	@Bean
	private UserDetailsService userDetailsService() {
		return username -> this.profRepo.findByUsername( username )
				.map(p -> new User(
					p.getUsername(), p.getPassword(), true, true, true, true,
					AuthorityUtils.createAuthorityList("USER", "write")
				))
				.orElseThrow( () -> {
					RuntimeException ex = new ProfessorNotFoundException( username );
					return new UsernameNotFoundException( ex.getMessage() );
				});
	}

}
