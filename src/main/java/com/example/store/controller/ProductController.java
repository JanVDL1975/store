package com.example.store.controller;
import java.util.List;
import lombok.RequiredArgsConstructor;


import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.ProductRepository;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productMapper.productsToProductDTOs(productRepository.findAll());
    }

    @GetMapping
    public ProductDTO getproductByID(@RequestParam long id) {
        return productMapper.productToProductDTO(productRepository.findProductById(id));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createProduct(@RequestBody Product product) {
        return productMapper.productToProductDTO(productRepository.save(product));
    }
}