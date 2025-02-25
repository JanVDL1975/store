package com.example.store.controller;

import com.example.store.dto.OrderDTO;
import com.example.store.exceptions.InvalidOrderDataException;
import com.example.store.mapper.OrderMapper;
import com.example.store.repository.CustomerRepository;
import com.example.store.repository.OrderRepository;

import com.example.store.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CustomerRepository customerRepository;

    @Autowired
    public OrderController(OrderService orderService, OrderRepository orderRepository, OrderMapper orderMapper, CustomerRepository customerRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> allOrders = orderService.getAllOrders();
        if (allOrders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allOrders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable long id) {
        return orderRepository.findById(id)
                .map(orderMapper::orderToOrderDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> geOrderById(@PathVariable Long orderId) {
        OrderDTO order = orderService.getOrderById(orderId);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(order, HttpStatus.OK);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createOrder(@RequestBody OrderDTO orderDTO) throws InvalidOrderDataException {
        return orderService.createOrder(orderDTO);
    }
}
