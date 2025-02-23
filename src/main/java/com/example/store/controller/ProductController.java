package com.example.store.controller;
import java.util.List;

import com.example.store.entity.Customer;
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

    public ProductController(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    // Get all products with their associated order IDs
    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productMapper.productsToProductDTOs(productRepository.findAll());
    }

    // Get a single product by ID with order IDs
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(productMapper::productToProductDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createProduct(@RequestBody Product product) {

        if (product.getId() != null && productRepository.existsById(product.getId())) {
            throw new IllegalArgumentException("Product with ID " + product.getId() + " already exists. Use update instead.");
        }

        // Check if orders is null
        /*if (product.getOrders() == null)  {
            throw new IllegalArgumentException("Orders must not be null");
        }*/

        // Set the fetched Customer to the Product
        //product.setOrders(product.getOrders());

        Product savedProduct = productRepository.save(product);
        // Save the Product and map it to DTO
        return productMapper.productToProductDTO(productRepository.save(savedProduct));
    }
}
