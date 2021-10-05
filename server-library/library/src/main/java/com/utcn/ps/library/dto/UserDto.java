package com.utcn.ps.library.dto;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.utcn.ps.library.model.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

	private Long id;

	@Email(message = "You must provide a valid email adrress.")
	@NotBlank(message = "Email field cannot be left blank.")
	private String email;

	@NotBlank(message = "Username field cannot be left blank.")
	private String username;

	@NotBlank(message = "Password field cannot be left blank.")
	private String password;

	private Role role;

	private List<Long> rentedBooksIds;

	private List<Long> wishedBooksIds;

	private List<RentalDto> rentals;

}
