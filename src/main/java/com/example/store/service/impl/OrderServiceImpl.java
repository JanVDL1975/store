package com.example.store.service.impl;

import com.example.store.dto.OrderDTO;
import com.example.store.entity.Order;
import com.example.store.exceptions.InvalidOrderDataException;
import com.example.store.exceptions.OrderNotFoundException;
import com.example.store.mapper.OrderMapper;
import com.example.store.repository.CustomerRepository;
import com.example.store.repository.OrderRepository;
import com.example.store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CustomerRepository customerRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, CustomerRepository customerRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
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

    public OrderDTO getOrderById(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            OrderDTO orderDTO = orderMapper.orderToOrderDTO(order);
            System.out.println(orderDTO); // Debug log
            return orderDTO;
        }
        throw new OrderNotFoundException("Order not found with ID: " + orderId);
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) throws InvalidOrderDataException {
        Order order = orderMapper.orderDTOToOrder(orderDTO);

        // Validate order data
        validateOrderData(order);

        // Process order (either update existing or create a new one)
        Order processedOrder = processOrder(order);

        // Save and return DTO
        Order savedOrder = orderRepository.save(processedOrder);
        return orderMapper.orderToOrderDTO(savedOrder);
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
        if (order.getId() != null) {
            return orderRepository.findById(order.getId())
                    .map(existingOrder -> {
                        existingOrder.setDescription(order.getDescription());
                        existingOrder.setProducts(order.getProducts() != null ? order.getProducts() : "");
                        return existingOrder;
                    })
                    .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + order.getId()));
        } else {
            Order newOrder = new Order();
            newOrder.setDescription(order.getDescription());
            newOrder.setCustomer(order.getCustomer()); // Associate with the customer
            newOrder.setProducts(order.getProducts() != null ? order.getProducts() : "");
            return newOrder;
        }
    }
}
