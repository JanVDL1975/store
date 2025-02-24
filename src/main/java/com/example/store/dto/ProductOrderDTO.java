package com.example.store.dto;

import lombok.Data;

import java.util.List;

@Data

public class ProductOrderDTO {
    private Long id;
    //private List<Long> ordersContainingProduct;
    private String ordersContainingProduct;
}