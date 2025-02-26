package com.example.store.controller;

import com.example.store.dto.CustomerDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.entity.Customer;
import com.example.store.entity.Order;
import com.example.store.exceptions.InvalidOrderDataException;
import com.example.store.mapper.CustomerMapper;
import com.example.store.mapper.OrderMapper;
import com.example.store.repository.CustomerRepository;
import com.example.store.repository.OrderRepository;
import com.example.store.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@ComponentScan(basePackageClasses = {OrderMapper.class, CustomerMapper.class})
class OrderControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderService orderService;

    private OrderRepository orderRepository;
    private CustomerRepository customerRepository;
    private OrderMapper orderMapper;

    private Order order;
    private OrderDTO orderDTO;
    private Customer customer;
    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");

        customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("John Doe");

        order = new Order();
        order.setId(1L);
        order.setDescription("Test Order");
        order.setCustomer(customer);

        orderDTO = new OrderDTO();
        orderDTO.setOrderId(1L);
        orderDTO.setOrderDescription("Test Order");
        orderDTO.setCustomer(customerDTO);
    }

    @Test
    void testCreateOrder_Success() throws Exception, InvalidOrderDataException {
        when(orderService.createOrder(any(OrderDTO.class))).thenReturn(orderDTO);

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").value(1L))
                .andExpect(jsonPath("$.orderDescription").value("Test Order"))
                .andExpect(jsonPath("$.customer.id").value(1L))
                .andExpect(jsonPath("$.customer.name").value("John Doe"));
    }

    @Test
    void testCreateOrder_InvalidData() throws Exception {
        OrderDTO invalidOrder = new OrderDTO();
        invalidOrder.setOrderDescription(""); // Invalid order with empty description

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidOrder)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllOrders_Success() throws Exception {
        when(orderService.getAllOrders()).thenReturn(List.of(orderDTO));

        mockMvc.perform(get("/order"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value(1L))
                .andExpect(jsonPath("$[0].orderDescription").value("Test Order"))
                .andExpect(jsonPath("$[0].customer.name").value("John Doe"));
    }

    @Test
    void testGetAllOrders_EmptyList() throws Exception {
        when(orderService.getAllOrders()).thenReturn(List.of());

        mockMvc.perform(get("/order"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetOrderById_Success() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(orderDTO);

        mockMvc.perform(get("/order/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1L))
                .andExpect(jsonPath("$.orderDescription").value("Test Order"))
                .andExpect(jsonPath("$.customer.name").value("John Doe"));
    }

    @Test
    void testGetOrderById_NotFound() throws Exception {
        when(orderService.getOrderById(2L)).thenReturn(null);

        mockMvc.perform(get("/order/{id}", 2L))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetOrderById_InvalidId() throws Exception {
        mockMvc.perform(get("/order/{id}", "invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetOrderById_NonExistingOrder() throws Exception {
        when(orderRepository.findById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/order/{id}", 2L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateOrder_WhenCustomerDoesNotExist() throws Exception {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isBadRequest());
    }
}
