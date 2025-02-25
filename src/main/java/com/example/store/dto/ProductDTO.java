package com.example.store.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductDTO {
    private Long id;
    private String description;
    private String orderIds; // Changed to String instead of a List<Long>

    public ProductDTO() {}

    public ProductDTO(Long id, String description, String orderIds) {
        this.id = id;
        this.description = description;
        this.orderIds = orderIds;
    }
}