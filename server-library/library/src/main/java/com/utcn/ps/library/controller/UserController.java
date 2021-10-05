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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.utcn.ps.library.dto.UserBookDto;
import com.utcn.ps.library.dto.UserDto;
import com.utcn.ps.library.exception.ApiExceptionResponse;
import com.utcn.ps.library.model.Role;
import com.utcn.ps.library.service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping
	public ResponseEntity<?> getPrincipal() throws ApiExceptionResponse {
		return ResponseEntity.status(HttpStatus.OK).body(userService.getPrincipal());
	}

	@PostMapping
	public ResponseEntity<?> register(@RequestBody @Valid UserDto userDto) throws ApiExceptionResponse {
		return ResponseEntity.status(HttpStatus.OK).body(userService.save(userDto));
	}

	@PostMapping("/wishlist")
	public ResponseEntity<?> addBookWishlist(@RequestBody UserBookDto userBookDto) throws ApiExceptionResponse {
		return ResponseEntity.status(HttpStatus.OK).body(userService.addBookToWishlist(userBookDto));
	}

	@DeleteMapping("/wishlist/{userId}/{bookId}")
	public ResponseEntity<?> removeBookFromWishList(@PathVariable("userId") Long userId,
			@PathVariable("bookId") Long bookId) throws ApiExceptionResponse {
		return ResponseEntity.status(HttpStatus.OK).body(userService.removeBookFromWishlist(userId, bookId));
	}

	@GetMapping("/wishlist/{userId}")
	public ResponseEntity<?> getBookWishlist(@PathVariable("userId") Long userId) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.getBookWishlist(userId));
	}

	@GetMapping("/role-user")
	public ResponseEntity<?> findAllNonAdmins() {
		return ResponseEntity.status(HttpStatus.OK).body(userService.findByRole(Role.ROLE_USER));
	}

	@PostMapping("/recommend")
	public ResponseEntity<?> recommendBook(@RequestBody String message, @RequestParam String username) {
		userService.recommendBook(message, username);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

}
