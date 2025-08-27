package com.springboot.RestServices.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.springboot.RestServices.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}

