package com.example.store.dto;

import lombok.Data;

@Data
public class OrderCustomerDTO {
    private Long orderId;
    private String orderDescription;
    private Long customerId;
    private String customerName;
    private String customerOrders;
}

