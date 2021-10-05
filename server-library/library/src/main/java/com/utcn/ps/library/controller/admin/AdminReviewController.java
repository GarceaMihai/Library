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

import com.utcn.ps.library.dto.ReviewDto;
import com.utcn.ps.library.exception.ApiExceptionResponse;
import com.utcn.ps.library.service.ReviewService;

@RestController
@RequestMapping("/admin/review")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminReviewController {

	@Autowired
	private ReviewService reviewService;

	@GetMapping
	public ResponseEntity<?> findAll() {
		return ResponseEntity.status(HttpStatus.OK).body(reviewService.findAll());
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody @Valid ReviewDto reviewDto) throws ApiExceptionResponse {
		return ResponseEntity.status(HttpStatus.OK).body(reviewService.save(reviewDto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		reviewService.delete(id);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> edit(@PathVariable("id") Long id, @RequestBody @Valid ReviewDto reviewDto)
			throws ApiExceptionResponse {
		return ResponseEntity.status(HttpStatus.OK).body(reviewService.update(id, reviewDto));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getReview(@PathVariable Long id) throws ApiExceptionResponse {
		return ResponseEntity.status(HttpStatus.OK).body(reviewService.findById(id));
	}

	@GetMapping("/export")
	public ResponseEntity<?> exportAll(@RequestParam String fileType) {
		return ResponseEntity.status(HttpStatus.OK).body(reviewService.exportAll(fileType));
	}

	@GetMapping("/export/{id}")
	public ResponseEntity<?> exportReview(@PathVariable("id") Long id, @RequestParam String fileType) {
		return ResponseEntity.status(HttpStatus.OK).body(reviewService.exportReview(id, fileType));
	}

}
