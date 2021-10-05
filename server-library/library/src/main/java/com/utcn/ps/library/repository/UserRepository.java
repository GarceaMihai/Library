package com.utcn.ps.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utcn.ps.library.model.Role;
import com.utcn.ps.library.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	public User findByUsername(String username);

	public User findByEmail(String email);

	public List<User> findByRole(Role role);
}
