package com.utcn.ps.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.utcn.ps.library.dto.BookRequestDto;
import com.utcn.ps.library.model.BookRequest;
import com.utcn.ps.library.model.User;

@Repository
public interface BookRequestRepository extends JpaRepository<BookRequest, Long> {
	public List<BookRequest> findByUser(User user);

	public BookRequest findByIsbn13AndUser(String isbn13, User user);

	@Query("SELECT new com.utcn.ps.library.dto.BookRequestDto(b.isbn13, COUNT(b.isbn13) as ct) FROM com.utcn.ps.library.model.BookRequest b GROUP BY b.isbn13 ORDER BY ct DESC")
	public List<BookRequestDto> findAllUnsatisifiedGroupByIsbn13OrderByCount();

	public long deleteByIsbn13(String isbn13);

}
