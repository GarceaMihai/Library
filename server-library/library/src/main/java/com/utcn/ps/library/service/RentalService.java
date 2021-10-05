package com.utcn.ps.library.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.utcn.ps.library.dto.BookDto;
import com.utcn.ps.library.dto.RentalDto;
import com.utcn.ps.library.exception.ApiExceptionResponse;

@Component
public interface RentalService {

	public BookDto rentOrReserve(RentalDto rentalDto) throws ApiExceptionResponse;

	public List<RentalDto> findByUser(Long id) throws ApiExceptionResponse;

	public List<RentalDto> findAll();

	public void delete(Long id);

	public RentalDto findById(Long id) throws ApiExceptionResponse;

	public RentalDto update(Long id, RentalDto rentalDto) throws ApiExceptionResponse;

	public String exportRental(Long id, String fileType);

	public String exportAll(String fileType);
}
