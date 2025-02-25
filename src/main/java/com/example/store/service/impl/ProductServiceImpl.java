package com.example.store.service.impl;

import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.OrderRepository;
import com.example.store.repository.ProductRepository;
import com.example.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final OrderRepository orderRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        return productMapper.productsToProductDTOs(productList);
    }

    @Override
    public ResponseEntity<ProductDTO> getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::productToProductDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setDescription("Product Description");
        product.setOrderIds(productDTO.getOrderIds() != null ? productDTO.getOrderIds() : "");

        Product savedProduct = productRepository.save(product);
        return productMapper.productToProductDTO(savedProduct);
    }
}

