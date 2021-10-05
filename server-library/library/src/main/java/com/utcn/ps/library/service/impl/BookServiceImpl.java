package com.utcn.ps.library.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.utcn.ps.library.dto.BookDto;
import com.utcn.ps.library.dto.BookFilterDto;
import com.utcn.ps.library.exception.ApiExceptionResponse;
import com.utcn.ps.library.model.Book;
import com.utcn.ps.library.model.FileType;
import com.utcn.ps.library.repository.BookRepository;
import com.utcn.ps.library.repository.BookRequestRepository;
import com.utcn.ps.library.service.BookService;
import com.utcn.ps.library.util.FilterUtil;
import com.utcn.ps.library.util.ListWrapper;
import com.utcn.ps.library.util.Mapper;
import com.utcn.ps.library.util.ValidationUtil;
import com.utcn.ps.library.util.exporter.FileExporter;
import com.utcn.ps.library.util.exporter.TXTFileExporter;
import com.utcn.ps.library.util.exporter.XMLFileExporter;

@Service
@Transactional
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BookRequestRepository bookRequestRepository;

	public Book save(BookDto bookDto) throws ApiExceptionResponse {
		ValidationUtil.validateBook(bookDto, bookRepository, -1L);
		Book book = new Book();
		Mapper.mapBook(book, bookDto);
		book = bookRepository.save(book);
		bookRequestRepository.deleteByIsbn13(book.getIsbn13());
		return book;
	}

	public List<BookDto> findAll() {
		return bookRepository.findAll().stream().map(book -> Mapper.mapBookToDto(book)).collect(Collectors.toList());
	}

	public void deleteBook(Long bookId) {
		bookRepository.deleteById(bookId);
	}

	public BookDto findById(Long id) throws ApiExceptionResponse {
		try {
			return Mapper.mapBookToDto(bookRepository.findById(id).get());
		} catch (NoSuchElementException e) {
			throw ApiExceptionResponse.builder().errors(Collections.singletonList("No book with id " + id))
					.message("Entity not found").httpStatus(HttpStatus.NOT_FOUND).build();
		}
	}

	public Book update(Long id, BookDto bookDto) throws ApiExceptionResponse {
		ValidationUtil.validateBook(bookDto, bookRepository, id);
		Book book = bookRepository.getOne(id);
		if (book == null) {
			throw ApiExceptionResponse.builder().errors(Collections.singletonList("No book with id " + id))
					.message("Entity not found").httpStatus(HttpStatus.NOT_FOUND).build();
		}
		Mapper.mapBook(book, bookDto);
		book = bookRepository.save(book);
		bookRequestRepository.deleteByIsbn13(book.getIsbn13());
		return book;
	}

	@Override
	public String exportBook(Long id, String fileType) {
		Book book = bookRepository.findById(id).get();
		FileExporter fileExporter;
		if (fileType.equals(FileType.XML)) {
			fileExporter = new XMLFileExporter();
			return fileExporter.exportData(book);
		} else if (fileType.equals(FileType.TXT)) {
			fileExporter = new TXTFileExporter();
			return fileExporter.exportData(book);
		}
		return null;
	}

	@Override
	public String exportAll(String fileType) {
		List<Book> books = bookRepository.findAll();
		ListWrapper<Book> listWrapper = new ListWrapper<>();
		listWrapper.setList(books);
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
	public List<BookDto> filter(BookFilterDto bookFilterDto) {
		List<Book> books = bookRepository.findAll();
		return FilterUtil.filterBooks(books, bookFilterDto);
	}

}
