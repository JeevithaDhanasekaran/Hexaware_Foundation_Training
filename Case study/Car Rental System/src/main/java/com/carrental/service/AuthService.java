package com.carrental.service;

import com.carrental.entity.Auth;
import com.carrental.exception.DatabaseConnectionException;
import com.carrental.exception.InvalidInputException;

public interface AuthService {
	boolean register(Auth auth) throws InvalidInputException, DatabaseConnectionException;
    Auth login(String username, String password) throws InvalidInputException, DatabaseConnectionException;
    boolean changePassword(int customerId, String newPassword) throws InvalidInputException;
    boolean deleteAuthByCustomerId(int customerId) throws DatabaseConnectionException;
}
