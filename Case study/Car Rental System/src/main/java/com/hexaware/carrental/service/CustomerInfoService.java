package com.hexaware.carrental.service;

import java.util.List;

import com.hexaware.carrental.entity.CustomerInfo;
import com.hexaware.carrental.exception.DatabaseConnectionException;
import com.hexaware.carrental.exception.InvalidInputException;

public interface CustomerInfoService {
	boolean addCustomerInfo(CustomerInfo customerInfo) throws InvalidInputException, DatabaseConnectionException;

	CustomerInfo findByCustomerId(int customerId) throws InvalidInputException;

	List<CustomerInfo> findAll() throws DatabaseConnectionException, InvalidInputException;

	boolean updateCustomerInfo(CustomerInfo customerInfo) throws InvalidInputException, DatabaseConnectionException;

	boolean deleteByCustomerId(int customerId) throws DatabaseConnectionException;

	CustomerInfo findCustomerIdByPhone(String phone);
}
