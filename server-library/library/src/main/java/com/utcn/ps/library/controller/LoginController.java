package com.utcn.ps.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utcn.ps.library.service.UserService;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {

	@Autowired
	private UserService userService;

	@GetMapping
	public ResponseEntity<?> login() {
		userService.setNrOfActiveUsers(userService.getNrOfActiveUsers() + 1);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@GetMapping("/logout")
	public ResponseEntity<?> logout() {
		userService.setNrOfActiveUsers(userService.getNrOfActiveUsers() - 1);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

}
