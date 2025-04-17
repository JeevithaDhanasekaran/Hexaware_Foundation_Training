package com.hexaware.carrental.service;

import com.hexaware.carrental.entity.Auth;
import com.hexaware.carrental.exception.DatabaseConnectionException;
import com.hexaware.carrental.exception.InvalidInputException;

public interface AuthService {
	boolean register(Auth auth) throws InvalidInputException, DatabaseConnectionException;

	Auth login(String username, String password) throws InvalidInputException, DatabaseConnectionException;

	boolean changePassword(int customerId, String newPassword) throws InvalidInputException;

	boolean deleteAuthByCustomerId(int customerId) throws DatabaseConnectionException;

	Auth getAuthByCustomerId(int customerId) throws DatabaseConnectionException;
}
