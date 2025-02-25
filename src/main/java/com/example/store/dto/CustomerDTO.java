package com.example.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerDTO {
    @NotNull(message = "Customer ID cannot be null")
    private Long id;
    @NotBlank(message = "Customer name cannot be blank")
    @Size(min = 2, max = 50, message = "Customer name must be between 2 and 50 characters")
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
