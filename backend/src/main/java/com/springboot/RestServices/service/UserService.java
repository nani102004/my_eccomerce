package com.springboot.RestServices.service;

import java.util.List;

import com.springboot.RestServices.exception.UserException;
import com.springboot.RestServices.model.User;



public interface UserService {
	
public User findUserById(Long userId) throws UserException;
	
	public User findUserProfileByJwt(String jwt) throws UserException;
	
	public List<User> findAllUsers();

}