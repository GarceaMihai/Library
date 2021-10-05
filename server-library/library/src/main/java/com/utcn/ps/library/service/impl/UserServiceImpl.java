package com.utcn.ps.library.service.impl;

import static java.util.Collections.singletonList;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.utcn.ps.library.dto.BookDto;
import com.utcn.ps.library.dto.UserBookDto;
import com.utcn.ps.library.dto.UserDto;
import com.utcn.ps.library.exception.ApiExceptionResponse;
import com.utcn.ps.library.model.Book;
import com.utcn.ps.library.model.FileType;
import com.utcn.ps.library.model.NotificationEndpoints;
import com.utcn.ps.library.model.Role;
import com.utcn.ps.library.model.User;
import com.utcn.ps.library.repository.BookRepository;
import com.utcn.ps.library.repository.UserRepository;
import com.utcn.ps.library.service.UserService;
import com.utcn.ps.library.util.AuthenticationUtil;
import com.utcn.ps.library.util.ListWrapper;
import com.utcn.ps.library.util.Mapper;
import com.utcn.ps.library.util.ValidationUtil;
import com.utcn.ps.library.util.exporter.FileExporter;
import com.utcn.ps.library.util.exporter.TXTFileExporter;
import com.utcn.ps.library.util.exporter.XMLFileExporter;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	private static int nrOfActiveUsers = 0;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private SimpMessagingTemplate template;

	@Bean
	public void initAdmin() {
		List<User> admins = userRepository.findByRole(Role.ROLE_ADMIN);
		if (admins == null || admins.isEmpty()) {
			User user = User.builder().email("office@company.com").username("admin")
					.password(passwordEncoder.encode("admin")).role(Role.ROLE_ADMIN).build();
			userRepository.save(user);
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Username " + username + " is not valid.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true,
				true, true, true, singletonList(new SimpleGrantedAuthority(user.getRole().toString())));
	}

	public User save(UserDto userDto) throws ApiExceptionResponse {
		ValidationUtil.validateUser(userDto, userRepository, -1L);
		User user = new User();
		Mapper.mapUser(user, userDto, passwordEncoder, false, false);
		user.setRole(Role.ROLE_USER);
		return userRepository.save(user);
	}

	public void delete(Long id) {
		userRepository.deleteById(id);
	}

	public User update(Long id, UserDto userDto, boolean changePassword) throws ApiExceptionResponse {
		if (changePassword) {
			User user = userRepository.getOne(id);
			Mapper.mapUser(user, userDto, passwordEncoder, false, true);
			return userRepository.save(user);
		} else {
			ValidationUtil.validateUser(userDto, userRepository, id);
			User user = userRepository.getOne(id);
			Mapper.mapUser(user, userDto, passwordEncoder, true, false);
			return userRepository.save(user);
		}
	}

	public List<UserDto> findAll() {
		return userRepository.findAll().stream().map(user -> Mapper.mapUserToDto(user)).collect(Collectors.toList());
	}

	public UserDto findById(Long id) {
		return Mapper.mapUserToDto(userRepository.findById(id).get());
	}

	public UserDto addBookToWishlist(UserBookDto userBookDto) throws ApiExceptionResponse {
		User user = userRepository.getOne(userBookDto.getUserId());
		Book book = bookRepository.getOne(userBookDto.getBookId());
		if (user.getBookWishlist().contains(book)) {
			throw ApiExceptionResponse.builder().errors(Collections.singletonList("Book already in wishlist."))
					.message("Book already in wishlist.").httpStatus(HttpStatus.METHOD_NOT_ALLOWED).build();
		}
		user.getBookWishlist().add(book);
		user = userRepository.save(user);
		return Mapper.mapUserToDto(user);
	}

	@Override
	public UserDto getPrincipal() throws ApiExceptionResponse {
		if (!AuthenticationUtil.isAuthenticated()) {
			throw ApiExceptionResponse.builder().errors(Collections.singletonList("Not authenticated."))
					.message("Not authenticated.").httpStatus(HttpStatus.UNAUTHORIZED).build();
		}
		User user = userRepository.findByUsername(
				((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
		return Mapper.mapUserToDto(user);
	}

	@Override
	public UserDto removeBookFromWishlist(Long userId, Long bookId) throws ApiExceptionResponse {
		User user = userRepository.getOne(userId);
		Book book = bookRepository.getOne(bookId);
		if (!user.getBookWishlist().contains(book)) {
			throw ApiExceptionResponse.builder().errors(Collections.singletonList("Book not in wishlist."))
					.message("Book not in wishlist.").httpStatus(HttpStatus.METHOD_NOT_ALLOWED).build();
		}
		user.getBookWishlist().remove(book);
		user = userRepository.save(user);
		return Mapper.mapUserToDto(user);
	}

	@Override
	public List<BookDto> getBookWishlist(Long id) {
		User user = userRepository.getOne(id);
		return user.getBookWishlist().stream().map(book -> Mapper.mapBookToDto(book)).collect(Collectors.toList());
	}

	@Override
	public int getNrOfActiveUsers() {
		return UserServiceImpl.nrOfActiveUsers;
	}

	@Override
	public void setNrOfActiveUsers(int nrOfActiveUsers) {
		UserServiceImpl.nrOfActiveUsers = nrOfActiveUsers;
	}

	@Override
	public String exportUser(Long id, String fileType) {
		User user = userRepository.findById(id).get();
		FileExporter fileExporter;
		if (fileType.equals(FileType.XML)) {
			fileExporter = new XMLFileExporter();
			return fileExporter.exportData(user);
		} else if (fileType.equals(FileType.TXT)) {
			fileExporter = new TXTFileExporter();
			return fileExporter.exportData(user);
		}
		return null;
	}

	@Override
	public String exportAll(String fileType) {
		List<User> users = userRepository.findAll();
		ListWrapper<User> listWrapper = new ListWrapper<>();
		listWrapper.setList(users);
		FileExporter fileExporter;
		if (fileType.equals(FileType.XML)) {
			fileExporter = new XMLFileExporter();
			return fileExporter.exportData(listWrapper);
		} else if (fileType.equals(FileType.TXT)) {
			fileExporter = new TXTFileExporter();
			return fileExporter.exportData(listWrapper);
		}
		return null;
	}

	@Override
	public List<UserDto> findByRole(Role role) {
		return userRepository.findByRole(role).stream().map(user -> Mapper.mapUserToDto(user))
				.collect(Collectors.toList());
	}

	@Override
	public void recommendBook(String message, String sendTo) {
		this.template.convertAndSend(
				NotificationEndpoints.BOOK_RECOMMENDATION + userRepository.findByUsername(sendTo).getId(), message);
	}

}
