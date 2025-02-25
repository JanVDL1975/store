package com.example.store.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {
    private Long orderId;  // Maps to orderId in the mapping
    private String orderDescription;  // Maps to orderDescription in the mapping
    private OrderCustomerDTO customer;
    private String products;
}
