package com.example.store.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY) // Only serialize non-empty fields
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @JsonIgnore // Ignoring orders in the Customer entity directly.
    @OneToMany(mappedBy = "customer", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<Order> customerOrders = new ArrayList<>();
}
