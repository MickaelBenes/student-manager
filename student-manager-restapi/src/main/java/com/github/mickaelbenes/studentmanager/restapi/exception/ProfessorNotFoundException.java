package com.github.mickaelbenes.studentmanager.restapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( HttpStatus.NOT_FOUND )
public class ProfessorNotFoundException extends RuntimeException {
	
	public ProfessorNotFoundException( String profUsername ) {
		super( "Could not find professor '" + profUsername + "'." );
	}

}
