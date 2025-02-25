package com.example.store.mapper;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;

import com.example.store.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    // Mapping customer to CustomerDTO
    @Mapping(source = "orders", target = "customerOrdersIds", qualifiedByName = "mapOrdersToIds")
    CustomerDTO customerToCustomerDTO(Customer customer);

    @Named("mapOrdersToIds")
    default String mapOrdersToIds(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return "";
        }
        return orders.stream()
                .map(order -> String.valueOf(order.getId())) // Get order IDs as strings
                .collect(Collectors.joining(", ")); // Join IDs as a single string
    }

    // Map list of customers to list of CustomerDTOs
    List<CustomerDTO> customersToCustomerDTOs(List<Customer> customer);
}


