package com.springboot.RestServices.service;

import com.springboot.RestServices.exception.ProductException;
import com.springboot.RestServices.model.Cart;
import com.springboot.RestServices.model.CartItem;
import com.springboot.RestServices.model.User;
import com.springboot.RestServices.request.AddItemRequest;

public interface CartService {
	
	public Cart createCart(User user);
	
	public CartItem addCartItem(Long userId,AddItemRequest req) throws ProductException;
	
	public Cart findUserCart(Long userId);
}
