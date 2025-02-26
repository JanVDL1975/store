package com.example.store.mapper;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.entity.Order;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CombinedMapperHelper {

    // Methods from StoreMapperHelper

    @Named("mapListOfItemsToStringStatic")
    public static String mapListOfItemsToStringStatic(List<String> items) {
        return (items != null && !items.isEmpty()) ? String.join(", ", items) : "";
    }

    @Named("mapStringToListOfItems")
    public static List<String> mapStringToListOfItems(String items) {
        return (items != null && !items.isEmpty()) ? Arrays.asList(items.split(",\\s*")) : new ArrayList<>();
    }

    public static String mapOrdersToString(List<Order> customerOrders) {
        this.mapListOfItemsToStringStatic();
    }

    // Methods from CustomerMapper

    @Named("orderCustomerId")
    public Long orderCustomerId(Customer customer) {
        return customer != null ? customer.getId() : null;
    }

    @Named("orderCustomerName")
    public String orderCustomerName(Customer customer) {
        return customer != null ? customer.getName() : null;
    }

    @Named("orderCustomerOrders")
    public String orderCustomerOrders(Customer customer) {
        if (customer == null || customer.getCustomerOrders() == null || customer.getCustomerOrders().isEmpty()) {
            return "";
        }
        return customer.getCustomerOrders().stream()
                .map(order -> "ID: " + order.getId() + ", Desc: " + order.getDescription())
                .collect(Collectors.joining(", "));
    }

    // Method to map Customer to CustomerDTO
    @Named("customerToCustomerDTO")
    public CustomerDTO customerToCustomerDTO(Customer customer) {
        if (customer == null) {
            return null;
        }

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());

        // Use mapOrdersToString method to convert the customer orders to a string
        customerDTO.setCustomerOrders(mapOrdersToString(customer.getCustomerOrders()));

        return customerDTO;
    }

    @Named("mapOrdersToIds")
    String mapOrdersToIds(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return "";
        }
        return orders.stream()
                .map(order -> String.valueOf(order.getId())) // Convert order ID to string
                .collect(Collectors.joining(", ")); // Join with commas
    }
}

