package com.example.store.repository;

import com.example.store.entity.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(Long customerId);

    // Fetch orders by a list of IDs
    List<Order> findByIdIn(List<Long> ids);
}

