package com.example.store.controller;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.entity.Order;
import com.example.store.mapper.CustomerMapper;
import com.example.store.repository.CustomerRepository;
import com.example.store.repository.OrderRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final OrderRepository orderRepository;

    @GetMapping
    public List<CustomerDTO> getAllCustomers() {
        return customerMapper.customersToCustomerDTOs(customerRepository.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerDTO>> getCustomersByQuery(@RequestParam String query) {
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<Customer> customers = (List<Customer>) customerRepository.findCustomersByNameContaining(query);
        if (customers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(customers.stream()
                .map(customerMapper::customerToCustomerDTO)
                .collect(Collectors.toList()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO createCustomer(@RequestBody Customer customer) {
        Customer tempCustomer = new Customer();
        Customer savedCustomer = null;

        if(!customer.getName().isEmpty())
        {
            tempCustomer.setName(customer.getName());
            savedCustomer = customerRepository.save(tempCustomer);
        }
        else {
            throw new IllegalArgumentException("Customer name cannot be empty! ");
        }

        if (customer.getId() != null && customerRepository.existsById(customer.getId())) {
            throw new IllegalArgumentException("Customer with ID " + customer.getId() + " already exists. Use update instead.");
        }

        // Ensure orders are linked correctly
        List<Order> orders = new ArrayList<>();
        String passedInProducts ="";

        if (customer.getOrders() != null) {
            for (Order order : customer.getOrders()) {
                if (order.getId() != null) {
                    Order existingOrder = orderRepository.findById(order.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + order.getId()));
                    Long savedCustomerId = savedCustomer.getId();
                    Long idAssociatedWithCustomer = existingOrder.getCustomer().getId();
                    // Check if the order is already associated with a customer
                    int result = idAssociatedWithCustomer.compareTo(savedCustomerId);
                    if(result!=0){
                        // Assume that it is illegal to change an order
                        throw new IllegalArgumentException("Order with ID " + order.getId() + " Is already associated with customer with ID " + idAssociatedWithCustomer);
                    }
                    else {
                        // Create an association if the order has no Customer assigned yet.
                        existingOrder.setCustomer(savedCustomer);
                    }
                    passedInProducts = existingOrder.getProducts();
                    existingOrder.setProducts(passedInProducts);
                    orders.add(existingOrder);
                } else {
                    order.setCustomer(customer);
                    List<Order> passedInOrders = customer.getOrders();
                    for (Order passedOrder:passedInOrders) {
                        passedInProducts = passedOrder.getProducts();
                    }

                    order.setProducts(passedInProducts);
                    orders.add(order);
                }
            }
        }

        customer.setOrders(orders);

        savedCustomer = customerRepository.save(customer);
        return customerMapper.customerToCustomerDTO(savedCustomer);
    }
    @PostMapping("/test")
    public ResponseEntity<String> handleJson(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok("Received: " + request);
    }
}
