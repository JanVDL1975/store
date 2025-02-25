package com.example.store.service;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerService {
    CustomerDTO getCustomer(Long customerId);
    CustomerDTO createCustomer(Customer customerDTO);
    List<CustomerDTO> getAllCustomers();
    public ResponseEntity<List<CustomerDTO>> getCustomersByQuery(String query);
}
