package com.springboot.RestServices.service;

import com.springboot.RestServices.exception.CartItemException;
import com.springboot.RestServices.exception.UserException;
import com.springboot.RestServices.model.Cart;
import com.springboot.RestServices.model.CartItem;
import com.springboot.RestServices.model.Product;

public interface CartItemService {
	public CartItem createCartItem(CartItem cartItem);

	public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException;

	public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId);

	public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException;

	public CartItem findCartItemById(Long cartItemId) throws CartItemException;

}
