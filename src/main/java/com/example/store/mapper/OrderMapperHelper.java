package com.example.store.mapper;

import com.example.store.entity.Customer;
import com.example.store.entity.Order;
import com.example.store.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Mapper
public class OrderMapperHelper {

    @Named("orderCustomerId")
    public Long orderCustomerId(Customer customer) {
        return customer != null ? customer.getId() : null;
    }

    @Named("orderCustomerName")
    public String orderCustomerName(Customer customer) {
        return customer != null ? customer.getName() : null;
    }

    @Named("orderCustomerOrders")
    public String orderCustomerOrders(Customer customer) {
        if (customer == null || customer.getCustomerOrders() == null || customer.getCustomerOrders().isEmpty()) {
            return "";
        }
        return customer.getCustomerOrders().stream()
                .map(order -> "ID: " + order.getId() + ", Desc: " + order.getDescription())
                .collect(Collectors.joining(", "));
    }

    @Named("mapOrdersToString")
    static String mapOrdersToString(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return "";
        }
        return orders.stream()
                .map(Order::getDescription) // Assuming Order has a getDescription() method
                .collect(Collectors.joining(", "));
    }

    @Named("mapProductsToString")
    public String mapProductsToString(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return "";
        }
        return products.stream()
                .map(product -> "ID: " + product.getId() + ", Desc: " + product.getDescription())
                .collect(Collectors.joining(", "));
    }
}

