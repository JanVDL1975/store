package com.example.store.mapper;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    List<CustomerDTO> customersToCustomerDTOs(List<Customer> customer);
    @Mapping(source = "orders", target = "orders", ignore = true)
    CustomerDTO customerToCustomerDTO(Customer customer);
}
