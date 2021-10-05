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

import com.utcn.ps.library.dto.RentalDto;
import com.utcn.ps.library.exception.ApiExceptionResponse;
import com.utcn.ps.library.service.RentalService;

@RestController
@RequestMapping("/admin/rental")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminRentalController {

	@Autowired
	private RentalService rentalService;

	@GetMapping
	public ResponseEntity<?> viewRentals() {
		return ResponseEntity.status(HttpStatus.OK).body(rentalService.findAll());
	}

	@PostMapping
	public ResponseEntity<?> createRental(@RequestBody @Valid RentalDto rentalDto) throws ApiExceptionResponse {
		return ResponseEntity.status(HttpStatus.OK).body(rentalService.rentOrReserve(rentalDto));
	}

	@DeleteMapping("/{rentalId}")
	public ResponseEntity<?> deleteRental(@PathVariable("rentalId") Long rentalId) {
		rentalService.delete(rentalId);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getRental(@PathVariable("id") Long id) throws ApiExceptionResponse {
		return ResponseEntity.status(HttpStatus.OK).body(rentalService.findById(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> editRental(@PathVariable("id") Long id, @RequestBody @Valid RentalDto rentalDto)
			throws ApiExceptionResponse {
		return ResponseEntity.status(HttpStatus.OK).body(rentalService.update(id, rentalDto));
	}

	@GetMapping("/export")
	public ResponseEntity<?> exportAll(@RequestParam String fileType) {
		return ResponseEntity.status(HttpStatus.OK).body(rentalService.exportAll(fileType));
	}

	@GetMapping("/export/{id}")
	public ResponseEntity<?> exportRental(@PathVariable("id") Long id, @RequestParam String fileType) {
		return ResponseEntity.status(HttpStatus.OK).body(rentalService.exportRental(id, fileType));
	}

}
