package com.springboot.RestServices.service;

import java.util.List;

import com.springboot.RestServices.exception.ProductException;
import com.springboot.RestServices.model.Review;
import com.springboot.RestServices.model.User;
import com.springboot.RestServices.request.ReviewRequest;

public interface ReviewService {
	public Review createReview(ReviewRequest req, User user) throws ProductException;

	public List<Review> getAllReview(Long productId);
}
