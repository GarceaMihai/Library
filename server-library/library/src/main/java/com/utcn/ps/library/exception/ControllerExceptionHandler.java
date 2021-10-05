package com.utcn.ps.library.exception;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

	protected ResponseEntity<Object> handleApiExceptionResponse(ApiExceptionResponse e) {
		HttpStatus httpStatus = e.getHttpStatus() != null ? e.getHttpStatus() : HttpStatus.INTERNAL_SERVER_ERROR;
		return responseEntityBuilder(ApiExceptionResponse.builder().errors(e.getErrors()).httpStatus(httpStatus)
				.message(e.getMessage()).build());
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return responseEntityBuilder(ApiExceptionResponse.builder()
				.errors(Collections.singletonList("Missing request parameter: " + ex.getParameterName()))
				.httpStatus(status).message("Missing request parameter").build());
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errors = new LinkedList<>();
		String className = ex.getParameter().getParameterType().getSimpleName();
		ex.getBindingResult().getAllErrors().forEach(objectError -> {
			String fieldName = ((FieldError) objectError).getField();
			String errorMessage = objectError.getDefaultMessage();
			errors.add(fieldName + " - " + errorMessage);
		});
		return responseEntityBuilder(ApiExceptionResponse.builder().errors(errors).httpStatus(status)
				.message("Bad request for class: " + className).build());
	}

	private ResponseEntity<Object> responseEntityBuilder(ApiExceptionResponse ex) {
		return new ResponseEntity<>(ex, ex.getHttpStatus());
	}

}
