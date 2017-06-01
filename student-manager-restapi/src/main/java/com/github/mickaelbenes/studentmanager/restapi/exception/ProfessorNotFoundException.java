package com.github.mickaelbenes.studentmanager.restapi.exception;

public class ProfessorNotFoundException extends RuntimeException {
	
	public ProfessorNotFoundException( String profUsername ) {
		super( "Could not find professor '" + profUsername + "'." );
	}

}
