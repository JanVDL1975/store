package com.example.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderDTO {
    @NotNull(message = "Order ID cannot be null")
    private Long orderId;  // Maps to orderId in the mapping

    @NotBlank(message = "Order description cannot be blank")
    private String orderDescription;  // Maps to orderDescription in the mapping

    @NotNull(message = "Customer cannot be null")
    private CustomerDTO customer; // This will hold the Customer information

    @NotEmpty(message = "Products must not be empty")
    private String products;
}
