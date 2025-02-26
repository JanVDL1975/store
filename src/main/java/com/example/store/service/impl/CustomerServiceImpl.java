package com.example.store.service.impl;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.entity.Order;
import com.example.store.exceptions.CustomerNotFoundException;
import com.example.store.exceptions.InvalidCustomerDataException;
import com.example.store.exceptions.OrderBelongsToAnotherCustomerException;
import com.example.store.exceptions.OrderNotFoundException;
import com.example.store.mapper.CombinedMapperHelper;
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
    private final CombinedMapperHelper combinedMapperHelper;
    private final OrderRepository orderRepository;

    // Constructor injection for dependencies
    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository,
                               OrderRepository orderRepository,
                               CombinedMapperHelper combinedMapperHelper) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.combinedMapperHelper = combinedMapperHelper;
    }

    @Override
    public CustomerDTO getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .map(combinedMapperHelper::customerToCustomerDTO)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
    }

    @Override
    public CustomerDTO createCustomer(Customer customer) {
        validateCustomerData(customer);

        // Save the customer first
        Customer savedCustomer = customerRepository.save(customer);

        List<Order> managedOrders = new ArrayList<>();
        if (customer.getCustomerOrders() != null) {
            for (Order order : customer.getCustomerOrders()) {
                Order managedOrder = processOrder(order, savedCustomer);
                managedOrders.add(managedOrder);
            }
        }

        // Set orders and save again
        savedCustomer.setCustomerOrders(managedOrders);
        savedCustomer = customerRepository.save(savedCustomer);

        return combinedMapperHelper.customerToCustomerDTO(savedCustomer);
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
        List<Customer> customers = customerRepository.findAll(); // Fetch all customers
        return customers.stream()
                .map(combinedMapperHelper::customerToCustomerDTO) // Map each customer to CustomerDTO
                .collect(Collectors.toList());
    }

    // Mapping customer entity to customer DTO and orders to string
    private CustomerDTO mapToCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());

        // Map orders to a comma-separated string of order descriptions
        String orderDescriptions = getOrderDescriptionsAsString(customer.getCustomerOrders());
        customerDTO.setCustomerOrdersIds(orderDescriptions);

        return customerDTO;
    }

    // Convert orders to a string representation of descriptions or IDs
    private String getOrderDescriptionsAsString(List<Order> orders) {
        return orders.stream()
                .map(order -> order.getDescription()) // Or use order.getId() or any other representation
                .collect(Collectors.joining(", "));
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
                .map(combinedMapperHelper::customerToCustomerDTO)
                .collect(Collectors.toList()));
    }
}


