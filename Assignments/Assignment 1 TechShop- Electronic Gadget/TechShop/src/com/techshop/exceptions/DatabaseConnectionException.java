package com.techshop.exceptions;

//Exception for database connection issues
public class DatabaseConnectionException extends Exception {
    public DatabaseConnectionException(String message) {
        super(message);
    }
}
