package com.springboot.RestServices.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.RestServices.model.OrderItem;
;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
