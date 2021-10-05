package com.utcn.ps.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utcn.ps.library.dto.BookFilterDto;
import com.utcn.ps.library.exception.ApiExceptionResponse;
import com.utcn.ps.library.service.BookService;

@RestController
@RequestMapping("/book")
@CrossOrigin(origins = "http://localhost:4200")
public class BookController {

	@Autowired
	private BookService bookService;

	@GetMapping
	public ResponseEntity<?> viewAllBooks() {
		return ResponseEntity.status(HttpStatus.OK).body(bookService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> viewBook(@PathVariable("id") Long id) throws ApiExceptionResponse {
		return ResponseEntity.status(HttpStatus.OK).body(bookService.findById(id));
	}

	@PostMapping("/filter")
	public ResponseEntity<?> filterBooks(@RequestBody BookFilterDto bookFilterDto) {
		return ResponseEntity.status(HttpStatus.OK).body(bookService.filter(bookFilterDto));
	}

}
