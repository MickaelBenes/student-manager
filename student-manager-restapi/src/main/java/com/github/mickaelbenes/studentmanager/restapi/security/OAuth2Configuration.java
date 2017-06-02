package com.github.mickaelbenes.studentmanager.restapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

@Configuration
@EnableResourceServer
@EnableAuthorizationServer
public class OAuth2Configuration extends AuthorizationServerConfigurerAdapter {
	
	private static final String APPLICATION_NAME = "student-manager";
	
	// This is required for password grants, which we specify below as one of the
	// {@literal authorizedGrantTypes()}.
	@Autowired
	AuthenticationManagerBuilder authManager;
	
	@Override
	public void configure( AuthorizationServerEndpointsConfigurer endpoints ) throws Exception {
		// Workaround for https://github.com/spring-projects/spring-boot/issues/1801
		endpoints.authenticationManager( (Authentication authentication) -> this.authManager.getOrBuild().authenticate(authentication) );
	}

	@Override
	public void configure( ClientDetailsServiceConfigurer clients ) throws Exception {
		clients.inMemory()
				.withClient( "android_" + APPLICATION_NAME )
				.authorizedGrantTypes( "password", "authorization_code", "refresh_token" )
				.authorities( "ROLE_USER" )
				.scopes( "write" )
				.resourceIds( APPLICATION_NAME )
				.secret( "123456" );
	}

}
