package com.utcn.ps.library.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.utcn.ps.library.dto.BookRequestDto;
import com.utcn.ps.library.exception.ApiExceptionResponse;
import com.utcn.ps.library.model.Book;
import com.utcn.ps.library.model.BookRequest;
import com.utcn.ps.library.model.FileType;
import com.utcn.ps.library.model.User;
import com.utcn.ps.library.repository.BookRepository;
import com.utcn.ps.library.repository.BookRequestRepository;
import com.utcn.ps.library.repository.UserRepository;
import com.utcn.ps.library.service.BookRequestService;
import com.utcn.ps.library.util.ListWrapper;
import com.utcn.ps.library.util.Mapper;
import com.utcn.ps.library.util.ValidationUtil;
import com.utcn.ps.library.util.exporter.FileExporter;
import com.utcn.ps.library.util.exporter.TXTFileExporter;
import com.utcn.ps.library.util.exporter.XMLFileExporter;

@Service
@Transactional
public class BookRequestServiceImpl implements BookRequestService {

	@Autowired
	private BookRequestRepository bookRequestRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public BookRequestDto save(BookRequestDto bookRequestDto) throws ApiExceptionResponse {
		User user = userRepository.getOne(bookRequestDto.getUserId());
		if (user == null) {
			throw ApiExceptionResponse.builder()
					.errors(Collections.singletonList("No user with id " + bookRequestDto.getUserId()))
					.message("Entity not found").httpStatus(HttpStatus.NOT_FOUND).build();
		}
		Book book = bookRepository.findByIsbn13(bookRequestDto.getIsbn13());
		BookRequest existingBookRequest = bookRequestRepository.findByIsbn13AndUser(bookRequestDto.getIsbn13(), user);
		ValidationUtil.validateBookRequest(existingBookRequest, bookRequestDto, book, -1L);
		BookRequest bookRequest = new BookRequest();
		Mapper.mapBookRequest(bookRequest, bookRequestDto, user);
		return Mapper.mapBookRequestToDto(bookRequestRepository.save(bookRequest));
	}

	@Override
	public void delete(Long id) {
		bookRequestRepository.deleteById(id);
	}

	@Override
	public BookRequestDto update(Long id, BookRequestDto bookRequestDto) throws ApiExceptionResponse {
		User user = userRepository.getOne(bookRequestDto.getUserId());
		if (user == null) {
			throw ApiExceptionResponse.builder()
					.errors(Collections.singletonList("No user with id " + bookRequestDto.getUserId()))
					.message("Entity not found").httpStatus(HttpStatus.NOT_FOUND).build();
		}
		Book book = bookRepository.findByIsbn13(bookRequestDto.getIsbn13());
		BookRequest existingBookRequest = bookRequestRepository.findByIsbn13AndUser(bookRequestDto.getIsbn13(), user);
		ValidationUtil.validateBookRequest(existingBookRequest, bookRequestDto, book, id);
		BookRequest bookRequest = bookRequestRepository.getOne(id);
		if (bookRequest == null) {
			throw ApiExceptionResponse.builder().errors(Collections.singletonList("No book qequest with id " + id))
					.message("Entity not found").httpStatus(HttpStatus.NOT_FOUND).build();
		}
		Mapper.mapBookRequest(bookRequest, bookRequestDto, user);
		return Mapper.mapBookRequestToDto(bookRequestRepository.save(bookRequest));
	}

	@Override
	public BookRequestDto findById(Long id) {
		BookRequest bookRequest = bookRequestRepository.findById(id).get();
		return Mapper.mapBookRequestToDto(bookRequest);
	}

	@Override
	public List<BookRequestDto> findAll() {
		List<BookRequest> bookRequests = bookRequestRepository.findAll();
		return bookRequests.stream().map(bookRequest -> Mapper.mapBookRequestToDto(bookRequest))
				.collect(Collectors.toList());
	}

	@Override
	public List<BookRequestDto> findByUser(Long userId) {
		User user = userRepository.findById(userId).get();
		List<BookRequest> bookRequests = bookRequestRepository.findByUser(user);
		return bookRequests.stream().map(bookRequest -> Mapper.mapBookRequestToDto(bookRequest))
				.collect(Collectors.toList());
	}

	@Override
	public String exportBookRequest(Long id, String fileType) {
		BookRequest bookRequest = bookRequestRepository.findById(id).get();
		FileExporter fileExporter;
		if (fileType.equals(FileType.XML)) {
			fileExporter = new XMLFileExporter();
			return fileExporter.exportData(bookRequest);
		} else if (fileType.equals(FileType.TXT)) {
			fileExporter = new TXTFileExporter();
			return fileExporter.exportData(bookRequest);
		}
		return null;
	}

	@Override
	public String exportAll(String fileType) {
		List<BookRequest> bookRequests = bookRequestRepository.findAll();
		ListWrapper<BookRequest> listWrapper = new ListWrapper<>();
		listWrapper.setList(bookRequests);
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
	public List<BookRequestDto> findAllUnsatisifiedGroupByIsbn13OrderByCount() {
		return bookRequestRepository.findAllUnsatisifiedGroupByIsbn13OrderByCount();
	}

	@Override
	public long deleteByIsbn13(String isbn13) {
		return bookRequestRepository.deleteByIsbn13(isbn13);
	}

}
