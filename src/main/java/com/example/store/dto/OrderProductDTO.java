package com.example.store.dto;

import lombok.Data;

import java.util.List;

@Data

public class OrderProductDTO {
    private Long id;
    private String description;
    private String products;
}