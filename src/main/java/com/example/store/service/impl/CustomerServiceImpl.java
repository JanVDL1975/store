package com.example.store.service.impl;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.entity.Order;
import com.example.store.exceptions.CustomerNotFoundException;
import com.example.store.exceptions.InvalidCustomerDataException;
import com.example.store.exceptions.OrderBelongsToAnotherCustomerException;
import com.example.store.exceptions.OrderNotFoundException;
import com.example.store.mapper.CustomerMapper;
import com.example.store.repository.CustomerRepository;
import com.example.store.repository.OrderRepository;
import com.example.store.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final OrderRepository orderRepository;

    // Constructor injection for dependencies
    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository,
                               OrderRepository orderRepository,
                               CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerDTO getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .map(customerMapper::customerToCustomerDTO)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
    }

    @Override
    public CustomerDTO createCustomer(Customer customer) {
        validateCustomerData(customer);

        // Save the customer first
        Customer savedCustomer = customerRepository.save(customer);

        List<Order> managedOrders = new ArrayList<>();
        if (customer.getOrders() != null) {
            for (Order order : customer.getOrders()) {
                Order managedOrder = processOrder(order, savedCustomer);
                managedOrders.add(managedOrder);
            }
        }

        // Set orders and save again
        savedCustomer.setOrders(managedOrders);
        savedCustomer = customerRepository.save(savedCustomer);

        return customerMapper.customerToCustomerDTO(savedCustomer);
    }

    private void validateCustomerData(Customer customer) {
        if (customer.getName() == null || customer.getName().isEmpty()) {
            throw new InvalidCustomerDataException("Customer name cannot be empty!");
        }
    }

    private Order processOrder(Order order, Customer savedCustomer) {
        if (order.getId() != null) {
            return orderRepository.findById(order.getId())
                    .map(managedOrder -> {
                        if (managedOrder.getCustomer() != null &&
                                !managedOrder.getCustomer().getId().equals(savedCustomer.getId())) {
                            throw new OrderBelongsToAnotherCustomerException(
                                    "Order ID " + order.getId() + " belongs to another customer!");
                        }
                        return managedOrder;
                    })
                    .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + order.getId()));
        } else {
            Order managedOrder = new Order();
            managedOrder.setDescription(order.getDescription());
            managedOrder.setCustomer(savedCustomer);
            managedOrder.setProducts(order.getProducts() != null ? order.getProducts() : "");
            return orderRepository.save(managedOrder);
        }
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        // Fetch all customers from the database
        List<Customer> customers = customerRepository.findAll();

        // For each customer, fetch the orders using the list of order IDs
        return customers.stream()
                .map(customer -> {
                    CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);

                    // Get the order IDs from the customer's orders
                    List<Long> orderIds = customer.getOrders().stream()
                            .map(Order::getId)
                            .collect(Collectors.toList());

                    // Fetch orders based on order IDs using the correct method
                    List<Order> orders = orderRepository.findByIdIn(orderIds);

                    // Map orders to their IDs as a comma-separated string
                    String customerOrdersIds = orders.stream()
                            .map(order -> String.valueOf(order.getId()))
                            .collect(Collectors.joining(", "));

                    customerDTO.setCustomerOrdersIds(customerOrdersIds);

                    return customerDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<List<CustomerDTO>> getCustomersByQuery(String query) {
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
}


