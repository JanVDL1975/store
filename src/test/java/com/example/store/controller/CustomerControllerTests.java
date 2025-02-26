package com.example.store.controller;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.mapper.CustomerMapper;
import com.example.store.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@ComponentScan(basePackageClasses = CustomerMapper.class)
class CustomerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    //@MockBean
    private CustomerService customerService;

    private Customer customer;
    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setName("John Doe");
        customer.setId(1L);

        customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("John Doe");
    }

    @Test
    void testCreateCustomer_Success() throws Exception {
        when(customerService.createCustomer(any(Customer.class))).thenReturn(customerDTO);

        mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testCreateCustomer_Failure() throws Exception {
        when(customerService.createCustomer(any(Customer.class))).thenReturn(null);

        mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllCustomers_Success() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(List.of(customerDTO));

        mockMvc.perform(get("/customer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    void testGetAllCustomers_EmptyList() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(List.of());

        mockMvc.perform(get("/customer"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetCustomerById_Success() throws Exception {
        when(customerService.getCustomerById(1L)).thenReturn(customerDTO);

        mockMvc.perform(get("/customer/{customerId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testGetCustomerById_NotFound() throws Exception {
        when(customerService.getCustomerById(2L)).thenReturn(null);

        mockMvc.perform(get("/customer/{customerId}", 2L))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetCustomersByQuery_Success() throws Exception {
        List<CustomerDTO> customers = List.of(customerDTO);
        when(customerService.getCustomersByQuery("John")).thenReturn(ResponseEntity.ok(customers));

        mockMvc.perform(get("/customer/search")
                        .param("query", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    void testGetCustomersByQuery_NoResults() throws Exception {
        when(customerService.getCustomersByQuery("NonExistent")).thenReturn(ResponseEntity.noContent().build());

        mockMvc.perform(get("/customer/search")
                        .param("query", "NonExistent"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testCreateCustomer_InvalidRequest() throws Exception {
        Customer invalidCustomer = new Customer();
        invalidCustomer.setName(""); // Invalid name

        mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCustomer)))
                .andExpect(status().isBadRequest());
    }
}
