package com.example.store.service;

import com.example.store.dto.ProductDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ProductService {
    public List<ProductDTO> getAllProducts();
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id);
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO);

}
