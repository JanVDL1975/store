package com.example.store.mapper;
import org.mapstruct.Mapper;

import com.example.store.dto.OrderCustomerDTO;
import com.example.store.entity.Order;
import com.example.store.dto.OrderProductDTO;
import com.example.store.dto.ProductOrderDTO;
import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public class ProductMapper {

    public ProductDTO productToProductDTO(Product product) {
        // If orderIds is null, return an empty list
        //List<Long> orderIds = Optional.ofNullable(product.getOrderIds()).orElse(Collections.emptyList());
        String orderIds = Optional.ofNullable(product.getOrderIds()).orElse("");

        return new ProductDTO(product.getId(), product.getDescription(), orderIds);
    }

    public List<ProductDTO> productsToProductDTOs(List<Product> products) {
        return products.stream()
                .map(this::productToProductDTO)
                .collect(Collectors.toList());
    }

    public Product productDTOToProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setDescription(productDTO.getDescription());
        product.setOrderIds(productDTO.getOrderIds()); // Map orderIds directly
        return product;
    }
}
