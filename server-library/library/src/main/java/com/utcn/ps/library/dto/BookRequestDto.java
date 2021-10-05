package com.utcn.ps.library.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookRequestDto {

	private Long id;

	@Positive(message = "User ID must be a positive integer.")
	private Long userId;

	private String bookTitle;

	@NotEmpty(message = "ISBN-13 field cannot be left empty.")
	private String isbn13;

	private long nrOfRequests;

	public BookRequestDto(String isbn13, long nrOfRequests) {
		this.isbn13 = isbn13;
		this.nrOfRequests = nrOfRequests;
	}

}
