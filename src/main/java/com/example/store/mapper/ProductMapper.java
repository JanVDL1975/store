package com.example.store.mapper;
import org.mapstruct.Mapper;

import com.example.store.dto.OrderCustomerDTO;
import com.example.store.entity.Order;
import com.example.store.dto.OrderProductDTO;
import com.example.store.dto.ProductOrderDTO;
import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public class ProductMapper {
    public ProductDTO productToProductDTO(Product product) {
        List<Long> orderIds = new ArrayList<>();

        if(product.getOrders() != null){
            orderIds = product.getOrders().stream()
                    .map(Order::getId)
                    .collect(Collectors.toList());
        }


        return new ProductDTO(product.getId(), product.getDescription(), orderIds);
    }

    public List<ProductDTO> productsToProductDTOs(List<Product> products) {
        return products.stream()
                .map(this::productToProductDTO)
                .collect(Collectors.toList());
    }

   // OrderProductDTO orderToOrderProductDTO(Order order);
}