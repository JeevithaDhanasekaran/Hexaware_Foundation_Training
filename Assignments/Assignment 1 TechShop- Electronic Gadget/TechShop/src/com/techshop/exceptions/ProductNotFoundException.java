package com.techshop.exceptions;


//Exception for when a product is not found
public class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
