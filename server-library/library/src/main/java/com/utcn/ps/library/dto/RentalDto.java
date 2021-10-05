package com.utcn.ps.library.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;

@Data
public class RentalDto implements Comparable<RentalDto> {

	private Long id;

	@Positive(message = "User ID must be a positive integer.")
	private Long userId;

	@Positive(message = "Book ID must be a positive integer.")
	private Long bookId;

	@NotEmpty(message = "Address field cannot be left empty.")
	private String address;

	@NotEmpty(message = "Address field cannot be left empty.")
	private String phone;

	@NotNull
	private boolean rent;

	private LocalDate validSince;
	private LocalDate validUntil;
	private BookDto bookDto;

	@Override
	public int compareTo(RentalDto o) {
		if (validUntil.isAfter(LocalDate.now())) {
			return 1;
		} else if (LocalDate.now().isAfter(validUntil)) {
			return -1;
		}
		return 0;
	}

}
