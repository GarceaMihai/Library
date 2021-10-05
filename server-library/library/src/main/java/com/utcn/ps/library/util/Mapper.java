package com.utcn.ps.library.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.utcn.ps.library.dto.BookDto;
import com.utcn.ps.library.dto.BookRequestDto;
import com.utcn.ps.library.dto.RentalDto;
import com.utcn.ps.library.dto.ReviewDto;
import com.utcn.ps.library.dto.UserDto;
import com.utcn.ps.library.model.Book;
import com.utcn.ps.library.model.BookRequest;
import com.utcn.ps.library.model.Rental;
import com.utcn.ps.library.model.Review;
import com.utcn.ps.library.model.User;

public class Mapper {

	public static void mapUser(User user, UserDto userDto, BCryptPasswordEncoder passwordEncoder, boolean update,
			boolean changePassword) {

		if (!changePassword) {
			user.setEmail(userDto.getEmail());
			user.setUsername(userDto.getUsername());
		}
		if (!update) {
			user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		}

	}

	public static UserDto mapUserToDto(User user) {
		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		userDto.setEmail(user.getEmail());
		userDto.setUsername(user.getUsername());
		userDto.setPassword("does-not-matter");
		userDto.setRole(user.getRole());
		List<Long> rentedBooksIds = new ArrayList<>();
		List<Long> wishedBooksIds = new ArrayList<>();
		for (Rental rental : user.getRentals()) {
			if (rental.getValidUntil().isAfter(LocalDate.now())) {
				rentedBooksIds.add(rental.getBook().getId());
			}
		}
		userDto.setRentedBooksIds(rentedBooksIds);
		for (Book book : user.getBookWishlist()) {
			wishedBooksIds.add(book.getId());
		}
		userDto.setWishedBooksIds(wishedBooksIds);
		List<RentalDto> rentals = new ArrayList<>();
		for (Rental rental : user.getRentals()) {
			rentals.add(Mapper.mapRentalToDto(rental));
		}
		userDto.setRentals(rentals);
		return userDto;
	}

	public static void mapBook(Book book, BookDto bookDto) {
		book.setTitle(bookDto.getTitle());
		book.setAuthor(bookDto.getAuthor());
		book.setLanguage(bookDto.getLanguage());
		book.setPublished(bookDto.getPublished());
		book.setNrOfPages(bookDto.getNrOfPages());
		book.setNrOfCopies(bookDto.getNrOfCopies());
		book.setIsbn13(bookDto.getIsbn13());
	}

	public static BookDto mapBookToDto(Book book) {
		BookDto bookDto = new BookDto();
		bookDto.setId(book.getId());
		bookDto.setTitle(book.getTitle());
		bookDto.setAuthor(book.getAuthor());
		bookDto.setLanguage(book.getLanguage());
		bookDto.setPublished(book.getPublished());
		bookDto.setNrOfPages(book.getNrOfPages());
		bookDto.setNrOfCopies(book.getNrOfCopies());
		bookDto.setIsbn13(book.getIsbn13());
		int nrOfActiveRentals = 0;
		for (Rental rental : book.getRentals()) {
			if (rental.getValidUntil().isAfter(LocalDate.now())) {
				nrOfActiveRentals++;
			}
		}
		bookDto.setNrOfActiveRentals(nrOfActiveRentals);
		return bookDto;
	}

	public static void mapRental(Rental rental, RentalDto rentalDto, boolean rent, int days, boolean update) {
		rental.setAddress(rentalDto.getAddress());
		rental.setPhone(rentalDto.getPhone());
		if (!update) {
			if (rent) {
				rental.setValidSince(LocalDate.now().plus(3, ChronoUnit.DAYS));
				rental.setValidUntil(LocalDate.now().plus(33, ChronoUnit.DAYS));
			} else {
				rental.setValidSince(LocalDate.now().plus(days + 3, ChronoUnit.DAYS));
				rental.setValidUntil(LocalDate.now().plus(days + 33, ChronoUnit.DAYS));
			}
		}
	}

	public static RentalDto mapRentalToDto(Rental rental) {
		RentalDto rentalDto = new RentalDto();
		rentalDto.setId(rental.getId());
		rentalDto.setUserId(rental.getUser().getId());
		rentalDto.setBookId(rental.getBook().getId());
		rentalDto.setAddress(rental.getAddress());
		rentalDto.setPhone(rental.getPhone());
		rentalDto.setValidSince(rental.getValidSince());
		rentalDto.setValidUntil(rental.getValidUntil());
		rentalDto.setBookDto(Mapper.mapBookToDto(rental.getBook()));
		return rentalDto;
	}

	public static void mapReview(Review review, ReviewDto reviewDto, User user, Book book) {
		review.setUser(user);
		review.setBook(book);
		review.setBody(reviewDto.getBody());
		review.setPosted(Instant.now());
	}

	public static ReviewDto mapReviewToDto(Review review) {
		ReviewDto reviewDto = new ReviewDto();
		reviewDto.setId(review.getId());
		reviewDto.setUserId(review.getUser().getId());
		reviewDto.setUsername(review.getUser().getUsername());
		reviewDto.setBookId(review.getBook().getId());
		reviewDto.setBody(review.getBody());
		return reviewDto;
	}

	public static void mapBookRequest(BookRequest bookRequest, BookRequestDto bookRequestDto, User user) {
		bookRequest.setBookTitle(bookRequestDto.getBookTitle());
		bookRequest.setIsbn13(bookRequestDto.getIsbn13());
		bookRequest.setUser(user);
	}

	public static BookRequestDto mapBookRequestToDto(BookRequest bookRequest) {
		BookRequestDto bookRequestDto = new BookRequestDto();
		bookRequestDto.setId(bookRequest.getId());
		bookRequestDto.setBookTitle(bookRequest.getBookTitle());
		bookRequestDto.setIsbn13(bookRequest.getIsbn13());
		bookRequestDto.setUserId(bookRequest.getUser().getId());
		return bookRequestDto;
	}

}
