package com.springboot.RestServices.service;

import java.util.List;

import com.springboot.RestServices.exception.ProductException;
import com.springboot.RestServices.model.Rating;
import com.springboot.RestServices.model.User;
import com.springboot.RestServices.request.RatingRequest;

public interface RatingServices {
	
	public Rating createRating(RatingRequest req,User user) throws ProductException;
	
	public List<Rating> getProductsRating(Long productId);
}
