package com.techshop.exceptions;

//Exception for when there is an issue processing an order
public class OrderProcessingException extends Exception {
    public OrderProcessingException(String message) {
        super(message);
    }
}
