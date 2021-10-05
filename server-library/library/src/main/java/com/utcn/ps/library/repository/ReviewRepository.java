package com.utcn.ps.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utcn.ps.library.model.Book;
import com.utcn.ps.library.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
	public List<Review> findByBook(Book book);
}
