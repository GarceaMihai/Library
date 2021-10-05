package com.utcn.ps.library.controller.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.utcn.ps.library.dto.BookDto;
import com.utcn.ps.library.exception.ApiExceptionResponse;
import com.utcn.ps.library.service.BookService;

@RestController
@RequestMapping("/admin/book")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminBookController {

	@Autowired
	private BookService bookService;

	@GetMapping
	public ResponseEntity<?> viewBooks() {
		return ResponseEntity.status(HttpStatus.OK).body(bookService.findAll());
	}

	@PostMapping
	public ResponseEntity<?> addBook(@RequestBody @Valid BookDto bookDto) throws ApiExceptionResponse {
		return ResponseEntity.status(HttpStatus.OK).body(bookService.save(bookDto));
	}

	@DeleteMapping("/{bookId}")
	public ResponseEntity<?> deleteBook(@PathVariable("bookId") Long bookId) {
		bookService.deleteBook(bookId);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> viewAndEditBook(@PathVariable("id") Long bookId) throws ApiExceptionResponse {
		return ResponseEntity.status(HttpStatus.OK).body(bookService.findById(bookId));
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> editBook(@PathVariable("id") Long bookId, @RequestBody @Valid BookDto bookDto)
			throws ApiExceptionResponse {
		return ResponseEntity.status(HttpStatus.OK).body(bookService.update(bookId, bookDto));
	}

	@GetMapping("/export")
	public ResponseEntity<?> exportAll(@RequestParam String fileType) {
		return ResponseEntity.status(HttpStatus.OK).body(bookService.exportAll(fileType));
	}

	@GetMapping("/export/{id}")
	public ResponseEntity<?> exportBook(@PathVariable("id") Long id, @RequestParam String fileType) {
		return ResponseEntity.status(HttpStatus.OK).body(bookService.exportBook(id, fileType));
	}

}
