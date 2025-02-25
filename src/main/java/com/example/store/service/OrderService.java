package com.example.store.service;


import com.example.store.dto.OrderDTO;
import com.example.store.entity.Order;
import com.example.store.exceptions.InvalidOrderDataException;

import java.util.List;

public interface OrderService {
    List<OrderDTO> getAllOrders();
    OrderDTO getOrderById(Long orderId);
    OrderDTO createOrder(Order order) throws InvalidOrderDataException;
}
