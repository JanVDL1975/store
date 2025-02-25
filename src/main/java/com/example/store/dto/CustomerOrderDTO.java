package com.example.store.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

@Data
public class CustomerOrderDTO {
    private Long id;
    private String description;
    @JsonBackReference
    private CustomerDTO customer;
}
