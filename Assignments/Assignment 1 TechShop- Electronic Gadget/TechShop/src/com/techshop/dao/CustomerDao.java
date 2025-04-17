package com.techshop.dao;

import java.util.List;

import com.techshop.entities.Customers;
import com.techshop.exceptions.CustomerNotFoundException;
import com.techshop.exceptions.DatabaseConnectionException;

public interface CustomerDao {
	
	//add a new customer
	boolean addCustomer(Customers customer) throws DatabaseConnectionException;
	
	//list customer details with customerID
	Customers getCustomerById(int customerID) throws CustomerNotFoundException, DatabaseConnectionException;
	
	//list all the customers
	List<Customers> listAllCustomer() throws DatabaseConnectionException;
	
	//update customer details
	void updateCustomer(Customers customer) throws CustomerNotFoundException, DatabaseConnectionException;
	
	//delete a customer based on customerID
	void deleteCustomer(int customerID) throws CustomerNotFoundException, DatabaseConnectionException;
	
	//count the number ord orders by a particular customer
	int getTotalOrdersByCustomer(int customerId) throws CustomerNotFoundException, DatabaseConnectionException;

	
}
