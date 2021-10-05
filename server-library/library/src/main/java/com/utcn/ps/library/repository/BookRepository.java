package com.utcn.ps.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utcn.ps.library.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	public Book findByIsbn13(String isbn13);
}
