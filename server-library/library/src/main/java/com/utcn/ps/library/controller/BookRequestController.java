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

import com.utcn.ps.library.dto.BookRequestDto;
import com.utcn.ps.library.exception.ApiExceptionResponse;
import com.utcn.ps.library.service.BookRequestService;

@RestController
@RequestMapping("/book-request")
@CrossOrigin(origins = "http://localhost:4200")
public class BookRequestController {

	@Autowired
	private BookRequestService bookRequestService;

	@PostMapping
	public ResponseEntity<?> saveBookRequest(@RequestBody @Valid BookRequestDto bookRequestDto)
			throws ApiExceptionResponse {
		return ResponseEntity.status(HttpStatus.OK).body(bookRequestService.save(bookRequestDto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteBookRequest(@PathVariable("id") Long id) {
		bookRequestService.delete(id);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<?> findBookRequestByUser(@PathVariable("userId") Long userId) {
		return ResponseEntity.status(HttpStatus.OK).body(bookRequestService.findByUser(userId));
	}

}
