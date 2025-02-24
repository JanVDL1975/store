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

    /*@PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createProduct(@RequestBody Product product) {
        Product unvalidatedProduct = new Product();

        if (product.getId() != null && productRepository.existsById(product.getId())) {
            throw new IllegalArgumentException("Product with ID " + product.getId() + " already exists. Use update instead.");
        }

        List<Long> orderIds = product.getOrderIds() != null ? product.getOrderIds() : new ArrayList<>();  // ✅ Avoid null
        List<Long> validOrderIds = new ArrayList<>();

        for (Long id : orderIds) {
            Order existingOrder = orderRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));

            validOrderIds.add(id);
        }

        if (product.getDescription() != null && !product.getDescription().isEmpty()) {
            unvalidatedProduct.setDescription(product.getDescription());
        }

        unvalidatedProduct.setOrderIds(validOrderIds);

        Product savedProduct = productRepository.save(unvalidatedProduct);
        return productMapper.productToProductDTO(savedProduct);
    }*/

    @PostMapping
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO) {
        // Convert ProductDTO to Product entity
        //Product product = productMapper.productDTOToProduct(productDTO);
        Product product = new Product();

        // Save Product
        product.setDescription("Product Description");
        product.setOrderIds(productDTO.getOrderIds() != null ? productDTO.getOrderIds() : "");

        //Product savedProduct = productRepository.save(product);
        Product savedProduct = productRepository.save(product);

        // Return ProductDTO
        return productMapper.productToProductDTO(product);
    }
}
