package com.utcn.ps.library.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.utcn.ps.library.dto.BookDto;
import com.utcn.ps.library.dto.BookFilterDto;
import com.utcn.ps.library.exception.ApiExceptionResponse;
import com.utcn.ps.library.model.Book;

@Component
public interface BookService {
	public Book save(BookDto bookDto) throws ApiExceptionResponse;

	public List<BookDto> findAll();

	public void deleteBook(Long bookId);

	public BookDto findById(Long id) throws ApiExceptionResponse;

	public Book update(Long id, BookDto bookDto) throws ApiExceptionResponse;

	public String exportBook(Long id, String fileType);

	public String exportAll(String fileType);

	public List<BookDto> filter(BookFilterDto bookFilterDto);
}
