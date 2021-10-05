package com.utcn.ps.library.controller;

import javax.validation.Valid;

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

import com.utcn.ps.library.dto.RentalDto;
import com.utcn.ps.library.exception.ApiExceptionResponse;
import com.utcn.ps.library.service.RentalService;

@RestController
@RequestMapping("/rental")
@CrossOrigin(origins = "http://localhost:4200")
public class RentalController {

	@Autowired
	private RentalService rentalService;

	@PostMapping
	public ResponseEntity<?> createRental(@RequestBody @Valid RentalDto rentalDto) throws ApiExceptionResponse {
		return ResponseEntity.status(HttpStatus.OK).body(rentalService.rentOrReserve(rentalDto));
	}

	@GetMapping("/{userId}")
	public ResponseEntity<?> getByUser(@PathVariable("userId") Long userId) throws ApiExceptionResponse {
		return ResponseEntity.status(HttpStatus.OK).body(rentalService.findByUser(userId));
	}

}
