package com.utcn.ps.library.dto;

import java.time.Year;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;

import lombok.Data;

@Data
public class BookDto {

	private Long id;

	@NotEmpty(message = "Title field cannot be left empty.")
	private String title;

	@NotEmpty(message = "Author field cannot be left empty.")
	private String author;

	@NotEmpty(message = "Language field cannot be left empty.")
	private String language;

	@PastOrPresent(message = "The year of publication must be in the past or present year.")
	private Year published;

	@Positive(message = "Number of copies must be a strictly positive number.")
	private int nrOfPages;

	@Positive(message = "Number of copies must be a strictly positive number.")
	private int nrOfCopies;

	@NotEmpty(message = "ISBN-13 field cannot be left empty.")
	private String isbn13;

	private int nrOfActiveRentals;

}
