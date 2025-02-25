package com.example.store.mapper;

import com.example.store.dto.OrderCustomerDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.entity.Customer;
import com.example.store.entity.Order;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    // Mapping from Order to OrderDTO
    @Mapping(source = "id", target = "orderId")  // Map Order's id to OrderDTO's orderId
    @Mapping(source = "description", target = "orderDescription")  // Map Order's description to OrderDTO's orderDescription
    @Mapping(source = "customer.id", target = "customer.customerId")  // Map Customer's id to OrderDTO's customerId
    @Mapping(source = "customer.name", target = "customer.customerName")  // Map Customer's name to OrderDTO's customerName
    @Mapping(source = "customer.orders", target = "customer.customerOrders")  // Map Customer's orders to a concatenated string of order IDs
    OrderDTO orderToOrderDTO(Order order);

    // Map list of orders to list of OrderDTOs
    List<OrderDTO> ordersToOrderDTOs(List<Order> orders);

    @Named("mapOrdersToString")
    default String mapOrdersToString(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return "No Orders";
        }
        return orders.stream()
                .map(order -> String.valueOf(order.getId())) // Get order IDs as strings
                .collect(Collectors.joining(", ")); // Join IDs as a single string
    }

    // Mapping from Order to OrderCustomerDTO
    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "description", target = "orderDescription")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.name", target = "customerName")
    @Mapping(source = "customer.orders", target = "customerOrders")
    OrderCustomerDTO orderToOrderCustomerDTO(Order order);

    // Helper method to convert List<Order> to a comma-separated string of order IDs
    default String map(List<Order> orders) {
        if (orders == null) {
            return "";
        }
        return orders.stream()
                .map(order -> String.valueOf(order.getId()))
                .collect(Collectors.joining(", "));
    }
}





