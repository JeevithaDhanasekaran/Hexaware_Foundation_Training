package com.carrental.service.implementations;

import java.sql.Connection;

import com.carrental.dao.AuthDao;
import com.carrental.dao.implementations.AuthDaoImpl;
import com.carrental.entity.Auth;
import com.carrental.exception.DatabaseConnectionException;
import com.carrental.exception.InvalidInputException;
import com.carrental.service.AuthService;
import com.carrental.util.DBconnection;

public class AuthServiceImpl implements AuthService{
	private AuthDao authDao;

	public AuthServiceImpl(String fileName) throws DatabaseConnectionException {
		Connection connection = DBconnection.getConnection(fileName);
		authDao = new AuthDaoImpl(connection);
	}

	@Override
	public boolean register(Auth auth) throws InvalidInputException, DatabaseConnectionException {
		if(auth == null) {
            throw new InvalidInputException("Auth Details Cannot Be Null");
        }
        return authDao.addAuth(auth);
	}

	@Override
	public Auth login(String username, String password) throws InvalidInputException, DatabaseConnectionException {
		if(username == null || password == null) {
            throw new InvalidInputException("Username or Password Cannot Be Null");
        }
        return authDao.findByUsernameAndPassword(username, password);
	}

	@Override
	public boolean changePassword(int customerId, String newPassword) throws InvalidInputException {
		if(newPassword == null || newPassword.trim().length() < 6) {
            throw new InvalidInputException("Password must be at least 6 characters");
        }
        return authDao.updatePassword(customerId, newPassword);
	}

	@Override
	public boolean deleteAuthByCustomerId(int customerId) throws DatabaseConnectionException {
		return authDao.deleteAuthByCustomerId(customerId);
	}
	
}
