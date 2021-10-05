package com.utcn.ps.library;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.utcn.ps.library.model.User;
import com.utcn.ps.library.repository.UserRepository;

@SpringBootTest
class LibraryApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void testJpa() {
		User user = User.builder().email("test@test.ro").username("TestTest").password("test").build();
		User testedUser = userRepository.save(user);
		Assertions.assertTrue(testedUser.getId() != null);
		Assertions.assertTrue(testedUser.getEmail().equals(user.getEmail()));
		Assertions.assertTrue(testedUser.getUsername().equals(user.getUsername()));
		Assertions.assertTrue(testedUser.getPassword().equals(user.getPassword()));
		testedUser = userRepository.findByUsername("TestTest");
		Assertions.assertTrue(testedUser != null);
		userRepository.delete(testedUser);
		user = userRepository.findByUsername("TestTest");
		Assertions.assertTrue(user == null);
	}

}
