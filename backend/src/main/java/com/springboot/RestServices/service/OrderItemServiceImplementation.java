package com.springboot.RestServices.service;

import org.springframework.stereotype.Service;

import com.springboot.RestServices.model.OrderItem;
import com.springboot.RestServices.repository.OrderItemRepository;

@Service
public class OrderItemServiceImplementation implements OrderItemService {

	private OrderItemRepository orderItemRepository;
	public OrderItemServiceImplementation(OrderItemRepository orderItemRepository) {
		this.orderItemRepository=orderItemRepository;
	}
	@Override
	public OrderItem createOrderItem(OrderItem orderItem) {
		
		return orderItemRepository.save(orderItem);
	}

}