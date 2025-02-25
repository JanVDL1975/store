package com.example.store.mapper;

import com.example.store.dto.OrderDTO;
import com.example.store.entity.Order;
import com.example.store.dto.OrderCustomerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "customer", target = "customer")  // Map customer as a whole
    @Mapping(source = "customer.orders", target = "customer.customerOrders", qualifiedByName = "mapOrdersToString")
    OrderDTO orderToOrderDTO(Order order);

    List<OrderDTO> ordersToOrderDTOs(List<Order> orders);

    @Named("mapOrdersToIds")
    default String mapOrdersToIds(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return "";
        }

        StringBuilder orderDescriptions = new StringBuilder();
        for (Order order : orders) {
            if (orderDescriptions.length() > 0) {
                orderDescriptions.append(", ");
            }
            orderDescriptions.append(order.getDescription());
        }

        return orderDescriptions.toString();
    }


    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.name", target = "customerName")
    @Mapping(source = "customer.orders", target = "customerOrders", qualifiedByName = "mapOrdersToIds")
    public default OrderCustomerDTO orderToOrderCustomerDTO(Order order) {
        if (order == null) {
            return null;
        }

        OrderCustomerDTO orderCustomerDTO = new OrderCustomerDTO();

        // Assuming these methods extract customer details from the Order
        orderCustomerDTO.setCustomerId(orderCustomerId(order));
        orderCustomerDTO.setCustomerName(orderCustomerName(order));

        // Mapping orders to a concatenated string
        orderCustomerDTO.setCustomerOrders(mapOrdersToString(orderCustomerOrders(order)));

        return orderCustomerDTO;
    }

    // Custom method to map the orders list to a concatenated string of descriptions
    @Named("mapOrdersToString")
    default String mapOrdersToString(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return "";
        }

        return orders.stream()
                .map(order -> "{ID: " + order.getId() + ", Desc: " + order.getDescription() + "}, ")
                .collect(Collectors.joining(", "));  // Join them into a single string
    }

    // Assuming these methods extract customer details from the Order entity
    private Long orderCustomerId(Order order) {
        return order.getCustomer() != null ? order.getCustomer().getId() : null;
    }

    private String orderCustomerName(Order order) {
        return order.getCustomer() != null ? order.getCustomer().getName() : null;
    }

    private List<Order> orderCustomerOrders(Order order) {
        return order.getCustomer() != null ? order.getCustomer().getOrders() : null;
    }

}
