package com.example.store.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

import lombok.Data;

@Entity
@Data
@Table(name = "\"order\"")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private String products;
}

