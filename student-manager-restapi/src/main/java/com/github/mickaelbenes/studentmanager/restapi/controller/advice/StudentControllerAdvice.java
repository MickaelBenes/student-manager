package com.github.mickaelbenes.studentmanager.restapi.controller.advice;

import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.github.mickaelbenes.studentmanager.restapi.exception.ProfessorNotFoundException;

@ControllerAdvice
public class StudentControllerAdvice {
	
	@ResponseBody
	@ExceptionHandler( ProfessorNotFoundException.class )
	@ResponseStatus( HttpStatus.NOT_FOUND )
	VndErrors professorNotFoundExceptionHandler( ProfessorNotFoundException ex ) {
		return new VndErrors( "error", ex.getMessage() );
	}

}
