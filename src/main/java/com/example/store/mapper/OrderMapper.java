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
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Logger logger = Logger.getLogger(OrderMapper.class.getName());

    @Mapping(source = "id", target = "orderId") // Mapping order ID
    @Mapping(source = "description", target = "orderDescription") // Mapping order description
    OrderDTO orderToOrderDTO(Order order);

    // Map list of orders to list of OrderDTOs
    List<OrderDTO> ordersToOrderDTOs(List<Order> orders);

    @Named("mapOrdersToString")
    default String mapOrdersToString(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            logger.info("mapOrdersToString called with empty or null list.");
            return "";
        }

        orders.forEach(order -> {
            logger.info("Processing Order - ID: " + order.getId() + ", Description: " + order.getDescription());
        });

        String result = orders.stream()
                .map(o -> "OrderID: " + o.getId() + ", Description: " + o.getDescription())
                .collect(Collectors.joining("; "));

        logger.info("Final Mapped Orders String: " + result);
        return result;
    }

    // Helper method to convert List<Order> to a comma-separated string of order IDs
    default String map(List<Order> orders) {
        if (orders == null) {
            return "";
        }
        return orders.stream()
                .map(order -> String.valueOf(order.getId()))
                .collect(Collectors.joining(", "));
    }

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "description", target = "orderDescription")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.name", target = "customerName")
    @Mapping(source = "customer.orders", target = "customerOrders", qualifiedByName = "mapOrdersToString")
    OrderCustomerDTO orderToOrderCustomerDTO(Order order);
}





