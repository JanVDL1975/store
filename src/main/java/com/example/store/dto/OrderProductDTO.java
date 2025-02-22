package com.example.store.dto;

import lombok.Data;

import java.util.List;

@Data

public class OrderProductDTO {
    private Long id;
    private List<ProductDTO> productsInOrder;
}