package com.utcn.ps.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utcn.ps.library.model.Book;
import com.utcn.ps.library.model.Rental;
import com.utcn.ps.library.model.User;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

	public List<Rental> findByUserAndBook(User user, Book book);

	public List<Rental> findByUser(User user);

}
