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

import com.utcn.ps.library.dto.BookRequestDto;
import com.utcn.ps.library.exception.ApiExceptionResponse;
import com.utcn.ps.library.service.BookRequestService;

@RestController
@RequestMapping("/admin/book-request")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminBookRequestController {

	@Autowired
	private BookRequestService bookRequestService;

	@GetMapping
	public ResponseEntity<?> findAllBookRequests() {
		return ResponseEntity.status(HttpStatus.OK).body(bookRequestService.findAll());
	}

	@PostMapping
	public ResponseEntity<?> saveBookRequest(@RequestBody @Valid BookRequestDto bookRequestDto)
			throws ApiExceptionResponse {
		return ResponseEntity.status(HttpStatus.OK).body(bookRequestService.save(bookRequestDto));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getBookRequest(@PathVariable("id") Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(bookRequestService.findById(id));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteBookRequest(@PathVariable("id") Long id) {
		bookRequestService.delete(id);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> editBookRequest(@PathVariable("id") Long id,
			@RequestBody @Valid BookRequestDto bookRequestDto) throws ApiExceptionResponse {
		return ResponseEntity.status(HttpStatus.OK).body(bookRequestService.update(id, bookRequestDto));
	}

	@GetMapping("/export")
	public ResponseEntity<?> exportAll(@RequestParam String fileType) {
		return ResponseEntity.status(HttpStatus.OK).body(bookRequestService.exportAll(fileType));
	}

	@GetMapping("/export/{id}")
	public ResponseEntity<?> exportBookRequest(@PathVariable("id") Long id, @RequestParam String fileType) {
		return ResponseEntity.status(HttpStatus.OK).body(bookRequestService.exportBookRequest(id, fileType));
	}

	@GetMapping("/unsatisfied-grouped")
	public ResponseEntity<?> findAllUnsatisifiedGroupByIsbn13OrderByCount() {
		return ResponseEntity.status(HttpStatus.OK)
				.body(bookRequestService.findAllUnsatisifiedGroupByIsbn13OrderByCount());
	}

	@DeleteMapping("unsatisfied-grouped/{isbn13}")
	public ResponseEntity<?> deleteByIsbn13(@PathVariable("isbn13") String isbn13) {
		return ResponseEntity.status(HttpStatus.OK).body(bookRequestService.deleteByIsbn13(isbn13));
	}

}
