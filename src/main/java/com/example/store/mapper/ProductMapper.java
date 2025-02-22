package com.example.store.mapper;
import org.mapstruct.Mapper;

import com.example.store.dto.OrderCustomerDTO;
import com.example.store.entity.Order;
import com.example.store.dto.OrderProductDTO;
import com.example.store.dto.ProductOrderDTO;
import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO productToProductDTO(Product product);

    List<ProductDTO> productsToProductDTOs(List<Product> products);

    OrderProductDTO orderToOrderProductDTO(Order order);
}