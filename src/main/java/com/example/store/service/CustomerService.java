package com.example.store.service;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {
    CustomerDTO getCustomerById(Long customerId);
    CustomerDTO createCustomer(Customer customerDTO);
    List<CustomerDTO> getAllCustomers();
    public ResponseEntity<List<CustomerDTO>> getCustomersByQuery(String query);
}
