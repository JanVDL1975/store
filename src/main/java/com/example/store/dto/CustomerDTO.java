package com.example.store.dto;

import lombok.Data;

@Data
public class CustomerDTO {
    private Long id;
    private String name;
    private String customerOrders;  // This will hold the string representation of orders

    // Implement the setter for customerOrders
    public void setCustomerOrdersIds(String orderDescriptions) {
        this.customerOrders = orderDescriptions;
    }

    public void setCustomerOrders(String customerOrders) {
        this.customerOrders = customerOrders;
    }

}
