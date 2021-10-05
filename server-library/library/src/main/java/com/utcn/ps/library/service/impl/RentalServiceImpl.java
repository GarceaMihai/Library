package com.utcn.ps.library.service.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.utcn.ps.library.dto.BookDto;
import com.utcn.ps.library.dto.RentalDto;
import com.utcn.ps.library.exception.ApiExceptionResponse;
import com.utcn.ps.library.model.Book;
import com.utcn.ps.library.model.FileType;
import com.utcn.ps.library.model.Rental;
import com.utcn.ps.library.model.User;
import com.utcn.ps.library.repository.BookRepository;
import com.utcn.ps.library.repository.RentalRepository;
import com.utcn.ps.library.repository.UserRepository;
import com.utcn.ps.library.service.RentalService;
import com.utcn.ps.library.util.ListWrapper;
import com.utcn.ps.library.util.Mapper;
import com.utcn.ps.library.util.ValidationUtil;
import com.utcn.ps.library.util.exporter.FileExporter;
import com.utcn.ps.library.util.exporter.TXTFileExporter;
import com.utcn.ps.library.util.exporter.XMLFileExporter;

@Service
@Transactional
public class RentalServiceImpl implements RentalService {

	@Autowired
	private RentalRepository rentalRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookRepository bookRepository;

	public BookDto rentOrReserve(RentalDto rentalDto) throws ApiExceptionResponse {
		Book book = bookRepository.getOne(rentalDto.getBookId());
		User user = userRepository.getOne(rentalDto.getUserId());
		if (user == null) {
			throw ApiExceptionResponse.builder()
					.errors(Collections.singletonList("No user with id " + rentalDto.getUserId()))
					.message("Entity not found").httpStatus(HttpStatus.NOT_FOUND).build();
		}
		if (book == null) {
			throw ApiExceptionResponse.builder()
					.errors(Collections.singletonList("No book with id " + rentalDto.getBookId()))
					.message("Entity not found").httpStatus(HttpStatus.NOT_FOUND).build();
		}
		List<Rental> rentals = rentalRepository.findByUserAndBook(user, book);
		ValidationUtil.validateRental(book, user, rentals, rentalDto.isRent());
		Rental rental = new Rental();
		if (!rentalDto.isRent()) {
			int minDays = Integer.MAX_VALUE;
			for (Rental r : book.getRentals()) {
				if (r.getValidUntil().isAfter(LocalDate.now())
						&& LocalDate.now().until(r.getValidUntil(), ChronoUnit.DAYS) < minDays) {
					minDays = (int) LocalDate.now().until(r.getValidUntil(), ChronoUnit.DAYS);
				}
			}
			Mapper.mapRental(rental, rentalDto, rentalDto.isRent(), minDays, false);
		} else {
			Mapper.mapRental(rental, rentalDto, rentalDto.isRent(), -1, false);
		}
		rental.setUser(user);
		rental.setBook(book);
		rentalRepository.save(rental);
		return Mapper.mapBookToDto(bookRepository.getOne(rentalDto.getBookId()));
	}

	@Override
	public List<RentalDto> findByUser(Long id) throws ApiExceptionResponse {
		User user = userRepository.getOne(id);
		if (user == null) {
			throw ApiExceptionResponse.builder().errors(Collections.singletonList("No user with id " + id))
					.message("Entity not found").httpStatus(HttpStatus.NOT_FOUND).build();
		}
		List<Rental> rentals = rentalRepository.findByUser(user);
		List<RentalDto> rentalsDtos = rentals.stream().map(rental -> Mapper.mapRentalToDto(rental))
				.collect(Collectors.toList());
		Collections.sort(rentalsDtos, Collections.reverseOrder());
		return rentalsDtos;
	}

	@Override
	public List<RentalDto> findAll() {
		return rentalRepository.findAll().stream().map(rental -> Mapper.mapRentalToDto(rental))
				.collect(Collectors.toList());
	}

	@Override
	public void delete(Long id) {
		rentalRepository.deleteById(id);
	}

	@Override
	public RentalDto findById(Long id) throws ApiExceptionResponse {
		Rental rental = rentalRepository.getOne(id);
		if (rental == null) {
			throw ApiExceptionResponse.builder().errors(Collections.singletonList("No rental with id " + id))
					.message("Entity not found").httpStatus(HttpStatus.NOT_FOUND).build();
		}
		return Mapper.mapRentalToDto(rentalRepository.getOne(id));
	}

	@Override
	public RentalDto update(Long id, RentalDto rentalDto) throws ApiExceptionResponse {
		Rental rental = rentalRepository.getOne(id);
		if (rental == null) {
			throw ApiExceptionResponse.builder().errors(Collections.singletonList("No rental with id " + id))
					.message("Entity not found").httpStatus(HttpStatus.NOT_FOUND).build();
		}
		Mapper.mapRental(rental, rentalDto, true, -1, true);
		return Mapper.mapRentalToDto(rentalRepository.save(rental));
	}

	@Override
	public String exportRental(Long id, String fileType) {
		Rental rental = rentalRepository.findById(id).get();
		FileExporter fileExporter;
		if (fileType.equals(FileType.XML)) {
			fileExporter = new XMLFileExporter();
			return fileExporter.exportData(rental);
		} else if (fileType.equals(FileType.TXT)) {
			fileExporter = new TXTFileExporter();
			return fileExporter.exportData(rental);
		}
		return null;
	}

	@Override
	public String exportAll(String fileType) {
		List<Rental> rentals = rentalRepository.findAll();
		ListWrapper<Rental> listWrapper = new ListWrapper<>();
		listWrapper.setList(rentals);
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
