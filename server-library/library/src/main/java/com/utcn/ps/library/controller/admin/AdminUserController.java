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

import com.utcn.ps.library.dto.UserDto;
import com.utcn.ps.library.exception.ApiExceptionResponse;
import com.utcn.ps.library.service.UserService;

@RestController
@RequestMapping("/admin/user")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminUserController {

	@Autowired
	private UserService userService;

	@GetMapping
	public ResponseEntity<?> viewUsers() {
		return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
	}

	@PostMapping
	public ResponseEntity<?> addUser(@RequestBody @Valid UserDto userDto) throws ApiExceptionResponse {
		return ResponseEntity.status(HttpStatus.OK).body(userService.save(userDto));
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {
		userService.delete(userId);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> viewUser(@PathVariable("id") Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> editUser(@PathVariable("id") Long id, @RequestBody @Valid UserDto userDto,
			@RequestParam("changePassword") String changePassword) throws ApiExceptionResponse {
		return ResponseEntity.status(HttpStatus.OK)
				.body(userService.update(id, userDto, Boolean.parseBoolean(changePassword)));
	}

	@GetMapping("/active")
	public ResponseEntity<?> getNrOfActiveUsers() {
		return ResponseEntity.status(HttpStatus.OK).body(userService.getNrOfActiveUsers());
	}

	@GetMapping("/export")
	public ResponseEntity<?> exportAll(@RequestParam String fileType) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.exportAll(fileType));
	}

	@GetMapping("/export/{id}")
	public ResponseEntity<?> exportUser(@PathVariable("id") Long id, @RequestParam String fileType) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.exportUser(id, fileType));
	}

}
