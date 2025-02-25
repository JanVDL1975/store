package com.example.store.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductDTO {
    private Long id;
    private String description;
    //private List<Long> orderIds;
    private String orderIds;

    public ProductDTO(){}

    public ProductDTO(Long id, String description, String orderIds) {
        this.id = id;
        this.description = description;
        this.orderIds = orderIds;
    }
}