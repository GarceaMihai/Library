package com.utcn.ps.library.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.utcn.ps.library.dto.ReviewDto;
import com.utcn.ps.library.exception.ApiExceptionResponse;

@Component
public interface ReviewService {

	public ReviewDto save(ReviewDto reviewDto) throws ApiExceptionResponse;

	public List<ReviewDto> findByBook(Long bookId);

	public void delete(Long id);

	public List<ReviewDto> findAll();

	public ReviewDto update(Long id, ReviewDto reviewDto) throws ApiExceptionResponse;

	public ReviewDto findById(Long id) throws ApiExceptionResponse;

	public String exportReview(Long id, String fileType);

	public String exportAll(String fileType);

}
