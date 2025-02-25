package com.example.store.exceptions;

public class OrderBelongsToAnotherCustomerException extends RuntimeException {
    public OrderBelongsToAnotherCustomerException(String message) {
        super(message);
    }
}
