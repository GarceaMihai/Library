package com.utcn.ps.library.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.utcn.ps.library.dto.ReviewDto;
import com.utcn.ps.library.exception.ApiExceptionResponse;
import com.utcn.ps.library.model.Book;
import com.utcn.ps.library.model.FileType;
import com.utcn.ps.library.model.Review;
import com.utcn.ps.library.model.User;
import com.utcn.ps.library.repository.BookRepository;
import com.utcn.ps.library.repository.ReviewRepository;
import com.utcn.ps.library.repository.UserRepository;
import com.utcn.ps.library.service.ReviewService;
import com.utcn.ps.library.util.ListWrapper;
import com.utcn.ps.library.util.Mapper;
import com.utcn.ps.library.util.exporter.FileExporter;
import com.utcn.ps.library.util.exporter.TXTFileExporter;
import com.utcn.ps.library.util.exporter.XMLFileExporter;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookRepository bookRepository;

	@Override
	public ReviewDto save(ReviewDto reviewDto) throws ApiExceptionResponse {
		Review review = new Review();
		User user = userRepository.getOne(reviewDto.getUserId());
		Book book = bookRepository.getOne(reviewDto.getBookId());
		if (user == null) {
			throw ApiExceptionResponse.builder()
					.errors(Collections.singletonList("No user with id " + reviewDto.getUserId()))
					.message("Entity not found").httpStatus(HttpStatus.NOT_FOUND).build();
		}
		if (book == null) {
			throw ApiExceptionResponse.builder()
					.errors(Collections.singletonList("No book with id " + reviewDto.getBookId()))
					.message("Entity not found").httpStatus(HttpStatus.NOT_FOUND).build();
		}
		Mapper.mapReview(review, reviewDto, user, book);
		return Mapper.mapReviewToDto(reviewRepository.save(review));
	}

	@Override
	public List<ReviewDto> findByBook(Long bookId) {
		List<Review> reviews = reviewRepository.findByBook(bookRepository.getOne(bookId));
		return reviews.stream().map(review -> Mapper.mapReviewToDto(review)).collect(Collectors.toList());
	}

	@Override
	public void delete(Long id) {
		reviewRepository.deleteById(id);
	}

	@Override
	public List<ReviewDto> findAll() {
		List<Review> reviews = reviewRepository.findAll();
		return reviews.stream().map(review -> Mapper.mapReviewToDto(review)).collect(Collectors.toList());
	}

	@Override
	public ReviewDto update(Long id, ReviewDto reviewDto) throws ApiExceptionResponse {
		Review review = reviewRepository.getOne(id);
		if (review == null) {
			throw ApiExceptionResponse.builder().errors(Collections.singletonList("No review with id " + id))
					.message("Entity not found").httpStatus(HttpStatus.NOT_FOUND).build();
		}
		User user = userRepository.getOne(reviewDto.getUserId());
		Book book = bookRepository.getOne(reviewDto.getBookId());
		if (user == null) {
			throw ApiExceptionResponse.builder()
					.errors(Collections.singletonList("No user with id " + reviewDto.getUserId()))
					.message("Entity not found").httpStatus(HttpStatus.NOT_FOUND).build();
		}
		if (book == null) {
			throw ApiExceptionResponse.builder()
					.errors(Collections.singletonList("No book with id " + reviewDto.getBookId()))
					.message("Entity not found").httpStatus(HttpStatus.NOT_FOUND).build();
		}
		Mapper.mapReview(review, reviewDto, user, book);
		return Mapper.mapReviewToDto(reviewRepository.save(review));
	}

	@Override
	public ReviewDto findById(Long id) throws ApiExceptionResponse {
		Review review = reviewRepository.getOne(id);
		if (review == null) {
			throw ApiExceptionResponse.builder().errors(Collections.singletonList("No review with id " + id))
					.message("Entity not found").httpStatus(HttpStatus.NOT_FOUND).build();
		}
		return Mapper.mapReviewToDto(review);
	}

	@Override
	public String exportReview(Long id, String fileType) {
		Review review = reviewRepository.findById(id).get();
		FileExporter fileExporter;
		if (fileType.equals(FileType.XML)) {
			fileExporter = new XMLFileExporter();
			return fileExporter.exportData(review);
		} else if (fileType.equals(FileType.TXT)) {
			fileExporter = new TXTFileExporter();
			return fileExporter.exportData(review);
		}
		return null;
	}

	@Override
	public String exportAll(String fileType) {
		List<Review> reviews = reviewRepository.findAll();
		ListWrapper<Review> listWrapper = new ListWrapper<>();
		listWrapper.setList(reviews);
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

}
