package com.hexaware.carrental.service;

import java.util.List;

import com.hexaware.carrental.entity.Customers;
import com.hexaware.carrental.exception.CustomerNotFoundException;
import com.hexaware.carrental.exception.DatabaseConnectionException;
import com.hexaware.carrental.exception.InvalidInputException;

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
