package com.utcn.ps.library.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;

@Data
public class ReviewDto {

	private Long id;

	@Positive(message = "User ID must be a positive integer.")
	private Long userId;

	private String username;

	@Positive(message = "Book ID must be a positive integer.")
	private Long bookId;

	@NotEmpty(message = "Body field cannot be left empty.")
	@NotNull(message = "Body cannot be null.")
	private String body;

}
