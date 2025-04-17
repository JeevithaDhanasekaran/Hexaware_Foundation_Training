package com.carrental.service.implementations;

import java.sql.Connection;

import com.carrental.dao.AuthDao;
import com.carrental.dao.implementations.AuthDaoImpl;
import com.carrental.entity.Auth;
import com.carrental.entity.CustomerInfo;
import com.carrental.exception.DatabaseConnectionException;
import com.carrental.exception.InvalidInputException;
import com.carrental.service.AuthService;
import com.carrental.service.CustomerInfoService;
import com.carrental.util.DBconnection;

public class AuthServiceImpl implements AuthService {
	private AuthDao authDao;
	private final CustomerInfoService customerInfoService;

	public AuthServiceImpl(String fileName) throws DatabaseConnectionException {
		Connection connection = DBconnection.getConnection(fileName);
		this.customerInfoService = new CustomerInfoServiceImpl(fileName);
		authDao = new AuthDaoImpl(connection);
	}

	@Override
	public boolean register(Auth auth) throws InvalidInputException, DatabaseConnectionException {
		if (auth == null || auth.getCustomers() == null) {
			throw new InvalidInputException("Auth or Customer details cannot be null.");
		}

		int customerId = auth.getCustomers().getCustomerId();
		if (customerId <= 0) {
			throw new InvalidInputException("Invalid customer ID.");
		}

		// Validate if customer info exists
		CustomerInfo info = customerInfoService.findByCustomerId(customerId);
		if (info == null) {
			throw new InvalidInputException(
					"Customer contact info not found. Please provide contact details before registering.");
		}

		return authDao.addAuth(auth);
	}

	@Override
	public Auth login(String username, String password) throws InvalidInputException, DatabaseConnectionException {
		if (username == null || password == null) {
			throw new InvalidInputException("Username or Password Cannot Be Null");
		}
		return authDao.findByUsernameAndPassword(username, password);
	}

	@Override
	public boolean changePassword(int customerId, String newPassword) throws InvalidInputException {
		if (newPassword == null || newPassword.trim().length() < 6) {
			throw new InvalidInputException("Password must be at least 6 characters");
		}
		return authDao.updatePassword(customerId, newPassword);
	}

	@Override
	public boolean deleteAuthByCustomerId(int customerId) throws DatabaseConnectionException {
		return authDao.deleteAuthByCustomerId(customerId);
	}

	@Override
	public Auth getAuthByCustomerId(int customerId) throws DatabaseConnectionException {
		return authDao.getAuthByCustomerId(customerId);
	}

}
