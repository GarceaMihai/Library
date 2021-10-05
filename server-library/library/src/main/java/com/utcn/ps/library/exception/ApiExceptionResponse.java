package com.utcn.ps.library.exception;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(value = { "stackTrace", "suppressed", "cause", "localizedMessage" })
public class ApiExceptionResponse extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -104527790398439764L;

	private final HttpStatus httpStatus;
	private final List<String> errors;

	@Builder
	public ApiExceptionResponse(String message, HttpStatus httpStatus, List<String> errors) {
		super(message);
		this.httpStatus = httpStatus;
		this.errors = errors;
	}

}
