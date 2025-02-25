package com.example.store.exceptions;

public class InvalidOrderDataException extends Throwable {
    public InvalidOrderDataException(String message) {
        super(message);
    }
}
