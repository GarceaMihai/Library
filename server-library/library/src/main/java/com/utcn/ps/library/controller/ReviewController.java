package com.utcn.ps.library.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utcn.ps.library.dto.ReviewDto;
import com.utcn.ps.library.exception.ApiExceptionResponse;
import com.utcn.ps.library.service.ReviewService;

@RestController
@RequestMapping("/review")
@CrossOrigin(origins = "http://localhost:4200")
public class ReviewController {

	@Autowired
	private ReviewService reviewService;

	@GetMapping("/{bookId}")
	public ResponseEntity<?> findByBook(@PathVariable("bookId") Long bookId) {
		return ResponseEntity.status(HttpStatus.OK).body(reviewService.findByBook(bookId));
	}

	@PostMapping
	public ResponseEntity<?> save(@RequestBody @Valid ReviewDto reviewDto) throws ApiExceptionResponse {
		return ResponseEntity.status(HttpStatus.OK).body(reviewService.save(reviewDto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		reviewService.delete(id);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

}
