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

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "description", target = "description")
    OrderDTO orderToOrderDTO(Order order);

    @Mapping(source = "orders", target = "customerOrders", qualifiedByName = "mapOrdersToString")
    OrderCustomerDTO orderToOrderCustomerDTO(Customer customer);

    @Named("mapOrdersToString")
    default String mapOrdersToString(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return "No Orders";
        }
        return orders.stream()
                .map(Order::getDescription) // Convert to a list of descriptions
                .collect(Collectors.joining(", ")); // Join as a single string
    }

    List<OrderDTO> ordersToOrderDTOs(List<Order> orders);
}



