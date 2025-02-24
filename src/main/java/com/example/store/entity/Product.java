package com.example.store.entity;

//import com.example.store.utils.LongListConverter;
import jakarta.persistence.*;

import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "\"product\"")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    //@Column(columnDefinition = "JSONB")
    //@Convert(converter = LongListConverter.class)
    private String orderIds;
}

