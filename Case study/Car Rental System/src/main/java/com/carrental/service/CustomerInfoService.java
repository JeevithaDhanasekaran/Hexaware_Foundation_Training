package com.carrental.service;

import java.util.List;

import com.carrental.entity.CustomerInfo;
import com.carrental.exception.DatabaseConnectionException;
import com.carrental.exception.InvalidInputException;

public interface CustomerInfoService {
	boolean addCustomerInfo(CustomerInfo customerInfo) throws InvalidInputException, DatabaseConnectionException;

	CustomerInfo findByCustomerId(int customerId) throws InvalidInputException;

	List<CustomerInfo> findAll() throws DatabaseConnectionException, InvalidInputException;

	boolean updateCustomerInfo(CustomerInfo customerInfo) throws InvalidInputException, DatabaseConnectionException;

	boolean deleteByCustomerId(int customerId) throws DatabaseConnectionException;

	CustomerInfo findCustomerIdByPhone(String phone);
}
