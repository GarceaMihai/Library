package com.utcn.ps.library.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.utcn.ps.library.dto.BookRequestDto;
import com.utcn.ps.library.exception.ApiExceptionResponse;

@Component
public interface BookRequestService {
	public BookRequestDto save(BookRequestDto bookRequestDto) throws ApiExceptionResponse;

	public void delete(Long id);

	public BookRequestDto update(Long id, BookRequestDto bookRequestDto) throws ApiExceptionResponse;

	public BookRequestDto findById(Long id);

	public List<BookRequestDto> findAll();

	public List<BookRequestDto> findByUser(Long userId);

	public String exportBookRequest(Long id, String fileType);

	public String exportAll(String fileType);

	public List<BookRequestDto> findAllUnsatisifiedGroupByIsbn13OrderByCount();

	public long deleteByIsbn13(String isbn13);
}
