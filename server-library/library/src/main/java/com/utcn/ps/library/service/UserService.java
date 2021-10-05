package com.utcn.ps.library.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.utcn.ps.library.dto.BookDto;
import com.utcn.ps.library.dto.UserBookDto;
import com.utcn.ps.library.dto.UserDto;
import com.utcn.ps.library.exception.ApiExceptionResponse;
import com.utcn.ps.library.model.Role;
import com.utcn.ps.library.model.User;

@Component
public interface UserService extends UserDetailsService {
	public User save(UserDto userDto) throws ApiExceptionResponse;

	public void delete(Long id);

	public User update(Long id, UserDto userDto, boolean changePassword) throws ApiExceptionResponse;

	public List<UserDto> findAll();

	public UserDto findById(Long id);

	public UserDto addBookToWishlist(UserBookDto userBookDto) throws ApiExceptionResponse;

	public UserDto getPrincipal() throws ApiExceptionResponse;

	public UserDto removeBookFromWishlist(Long userId, Long bookId) throws ApiExceptionResponse;

	public List<BookDto> getBookWishlist(Long id);

	public int getNrOfActiveUsers();

	public void setNrOfActiveUsers(int nrOfActiveUsers);

	public String exportUser(Long id, String fileType);

	public String exportAll(String fileType);

	public List<UserDto> findByRole(Role role);

	public void recommendBook(String message, String sendTo);
}
