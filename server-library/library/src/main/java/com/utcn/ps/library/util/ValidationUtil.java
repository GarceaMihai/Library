package com.utcn.ps.library.util;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.utcn.ps.library.dto.BookDto;
import com.utcn.ps.library.dto.BookRequestDto;
import com.utcn.ps.library.dto.UserDto;
import com.utcn.ps.library.exception.ApiExceptionResponse;
import com.utcn.ps.library.model.Book;
import com.utcn.ps.library.model.BookRequest;
import com.utcn.ps.library.model.Rental;
import com.utcn.ps.library.model.User;
import com.utcn.ps.library.repository.BookRepository;
import com.utcn.ps.library.repository.UserRepository;

public class ValidationUtil {

	public static void validateUser(UserDto userDto, UserRepository userRepository, Long id)
			throws ApiExceptionResponse {

		User userByEmail = userRepository.findByEmail(userDto.getEmail());
		User userByUsername = userRepository.findByUsername(userDto.getUsername());

		if ((userByEmail != null && userByEmail.getId() != id) || userDto.getEmail().equals("office@company.com")) {
			throw ApiExceptionResponse.builder().errors(Collections.singletonList("Email already in use."))
					.message("Email already in use.").httpStatus(HttpStatus.METHOD_NOT_ALLOWED).build();
		}
		if ((userByUsername != null && userByUsername.getId() != id) || userDto.getUsername().equals("admin")) {
			throw ApiExceptionResponse.builder().errors(Collections.singletonList("Username already in use."))
					.message("Username already in use.").httpStatus(HttpStatus.METHOD_NOT_ALLOWED).build();
		}

	}

	public static void validateBook(BookDto bookDto, BookRepository bookRepository, Long id)
			throws ApiExceptionResponse {

		Book bookByIsbn13 = bookRepository.findByIsbn13(bookDto.getIsbn13());

		if (bookByIsbn13 != null && bookByIsbn13.getId() != id) {
			throw ApiExceptionResponse.builder()
					.errors(Collections.singletonList("A book with this ISBN-13 is already in the database."))
					.message("A book with this ISBN-13 is already in the database.")
					.httpStatus(HttpStatus.METHOD_NOT_ALLOWED).build();
		}
	}

	public static void validateRental(Book book, User user, List<Rental> rentals, boolean rent)
			throws ApiExceptionResponse {
		int nrOfActiveRentals = 0;
		for (Rental rental : book.getRentals()) {
			if (rental.getValidUntil().isAfter(LocalDate.now())) {
				nrOfActiveRentals++;
			}
		}
		if ((!(book.getNrOfCopies() > nrOfActiveRentals) && rent == true)
				|| (!(book.getNrOfCopies() == nrOfActiveRentals) && rent == false)) {
			throw ApiExceptionResponse.builder().errors(Collections.singletonList("No copy available."))
					.message("No copy available.").httpStatus(HttpStatus.METHOD_NOT_ALLOWED).build();
		}
		for (Rental rental : rentals) {
			if (rental.getBook() == book && rental.getValidUntil().isAfter(LocalDate.now())) {
				throw ApiExceptionResponse.builder().errors(Collections.singletonList("Book already rented."))
						.message("Book already rented.").httpStatus(HttpStatus.METHOD_NOT_ALLOWED).build();
			}
		}
	}

	public static void validateBookRequest(BookRequest existingBookRequest, BookRequestDto bookRequestDto, Book book,
			Long id) throws ApiExceptionResponse {
		if (existingBookRequest != null && existingBookRequest.getId() != id) {
			throw ApiExceptionResponse.builder()
					.errors(Collections.singletonList("User with id " + bookRequestDto.getUserId()
							+ " already requested book with isbn-13 " + bookRequestDto.getIsbn13()))
					.message("User with id " + bookRequestDto.getUserId()
					+ " already requested book with isbn-13 " + bookRequestDto.getIsbn13()).httpStatus(HttpStatus.METHOD_NOT_ALLOWED).build();
		}
		if (book != null) {
			throw ApiExceptionResponse.builder()
					.errors(Collections.singletonList("A book with this ISBN-13 already exists in the database."))
					.message("A book with this ISBN-13 already exists in the database.").httpStatus(HttpStatus.METHOD_NOT_ALLOWED).build();
		}
	}

}
