package com.example.store.service.impl;

import com.example.store.dto.OrderDTO;
import com.example.store.entity.Order;
import com.example.store.exceptions.InvalidOrderDataException;
import com.example.store.exceptions.OrderNotFoundException;
import com.example.store.mapper.OrderMapper;
import com.example.store.repository.OrderRepository;
import com.example.store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        // Fetch all orders from the repository
        List<Order> orders = orderRepository.findAll();

        // Map each Order to an OrderDTO and collect them into a List
        return orders.stream()
                .map(orderMapper::orderToOrderDTO)  // Map each order to its corresponding DTO
                .collect(Collectors.toList());  // Collect mapped DTOs in a List
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::orderToOrderDTO)
                .orElseThrow(() -> new OrderNotFoundException("Order not found for id: " + orderId));
    }

    public OrderDTO createOrder(Order order) throws InvalidOrderDataException {
        // Validate the order data
        validateOrderData(order);

        // Process the order and assign the correct customer
        Order managedOrder = processOrder(order);

        // Save the order to the database
        managedOrder = orderRepository.save(managedOrder);

        // Return the mapped OrderDTO
        return orderMapper.orderToOrderDTO(managedOrder);
    }

    private void validateOrderData(Order order) throws InvalidOrderDataException {
        if (order.getDescription() == null || order.getDescription().isEmpty()) {
            throw new InvalidOrderDataException("Order description cannot be empty!");
        }
        if (order.getCustomer() == null) {
            throw new InvalidOrderDataException("Order must be associated with a customer!");
        }
    }

    private Order processOrder(Order order) {
        // If the order already has an ID, it should be updated
        if (order.getId() != null) {
            return orderRepository.findById(order.getId())
                    .map(existingOrder -> {
                        // You can add more checks or update the order fields here
                        existingOrder.setDescription(order.getDescription());
                        existingOrder.setProducts(order.getProducts() != null ? order.getProducts() : "");
                        return existingOrder;
                    })
                    .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + order.getId()));
        } else {
            // If the order doesn't have an ID, create a new one
            Order newOrder = new Order();
            newOrder.setDescription(order.getDescription());
            newOrder.setCustomer(order.getCustomer()); // Associate with the customer
            newOrder.setProducts(order.getProducts() != null ? order.getProducts() : "");
            return newOrder;
        }
    }
}
