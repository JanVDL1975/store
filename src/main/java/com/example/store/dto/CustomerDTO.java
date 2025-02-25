package com.example.store.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Setter;

import java.util.List;

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
