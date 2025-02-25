package com.example.store.controller;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.store.entity.Customer;
import com.example.store.entity.Order;
import com.example.store.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;


import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.ProductRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final OrderRepository orderRepository;

    public ProductController(ProductRepository productRepository, ProductMapper productMapper, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.orderRepository = orderRepository;
    }

    // Get all products with their associated order IDs
    @GetMapping
    public List<ProductDTO> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        return productMapper.productsToProductDTOs(productList);
    }

    // Get a single product by ID with order IDs
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(productMapper::productToProductDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO) {
        Product product = new Product();

        // Save Product
        product.setDescription("Product Description");
        product.setOrderIds(productDTO.getOrderIds() != null ? productDTO.getOrderIds() : "");

        Product savedProduct = productRepository.save(product);

        // Return ProductDTO
        return productMapper.productToProductDTO(product);
    }
}
