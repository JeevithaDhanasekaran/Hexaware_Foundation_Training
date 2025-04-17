package com.carrental.service;

import java.util.List;

import com.carrental.entity.Customers;
import com.carrental.exception.CustomerNotFoundException;
import com.carrental.exception.DatabaseConnectionException;
import com.carrental.exception.InvalidInputException;

public interface CustomersService {
	boolean addCustomer(Customers customer)
			throws InvalidInputException, DatabaseConnectionException, CustomerNotFoundException;

	Customers findCustomerById(int customerId) throws InvalidInputException;

	String getLicenseNumberByCustomerId(int customerId);

	List<Customers> getAllCustomers() throws InvalidInputException, DatabaseConnectionException;

	boolean updateCustomer(Customers customer) throws InvalidInputException;

	boolean deleteCustomer(int customerId);

	Customers addCustomer2(Customers customer) throws InvalidInputException, DatabaseConnectionException;

	int getCustomerIdByLicense(String licenseNumber) throws InvalidInputException, DatabaseConnectionException;

}
