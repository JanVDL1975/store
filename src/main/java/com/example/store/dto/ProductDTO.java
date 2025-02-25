package com.example.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductDTO {
    @NotNull(message = "Order ID cannot be null")
    private Long id;

    @NotBlank(message = "Order description cannot be blank")
    private String description;

    private String orderIds; // Changed to String instead of a List<Long>

    public ProductDTO() {}

    public ProductDTO(Long id, String description, String orderIds) {
        this.id = id;
        this.description = description;
        this.orderIds = orderIds;
    }
}