package com.example.store.mapper;

import com.example.store.dto.OrderCustomerDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.entity.Customer;
import com.example.store.entity.Order;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    static final ObjectMapper objectMapper = new ObjectMapper();
    //@Mapping(source = "products", target = "products")
    OrderDTO orderToOrderDTO(Order order);

    List<OrderDTO> ordersToOrderDTOs(List<Order> orders);

    //List<OrderDTO> ordersToOrderDTOs(String orders);
    public static List<OrderDTO> ordersToOrderDTOs(String orders) {
        try {
            return objectMapper.readValue(orders, new TypeReference<List<OrderDTO>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JSON string to List<OrderDTO>", e);
        }
    }

    OrderCustomerDTO orderToOrderCustomerDTO(Customer customer);
}
