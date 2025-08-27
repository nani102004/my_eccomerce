package com.springboot.RestServices.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.RestServices.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	public User findByEmail(String email);
	
	public List<User> findAllByOrderByCreatedAtDesc();

}

