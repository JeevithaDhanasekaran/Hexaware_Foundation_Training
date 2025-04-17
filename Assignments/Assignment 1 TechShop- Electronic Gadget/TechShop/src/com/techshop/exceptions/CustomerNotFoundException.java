package com.techshop.exceptions;

// Exception for when a customer is not found
public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
