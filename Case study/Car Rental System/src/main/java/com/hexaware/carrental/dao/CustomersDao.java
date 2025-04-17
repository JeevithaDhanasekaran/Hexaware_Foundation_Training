package com.hexaware.carrental.dao;

import java.util.List;

import com.hexaware.carrental.entity.Customers;
import com.hexaware.carrental.exception.CustomerNotFoundException;
import com.hexaware.carrental.exception.DatabaseConnectionException;
import com.hexaware.carrental.exception.InvalidInputException;

public interface CustomersDao {
	boolean addCustomer(Customers customer) throws DatabaseConnectionException, CustomerNotFoundException;
	
    Customers findById(int customerId) throws InvalidInputException;
    
    String getLicenseNumberByCustomerId(int customerId);//get the license number by customer ID
    
    List<Customers> findAllCustomers() throws InvalidInputException;//list all customers irrespective of 

    boolean updateCustomer(Customers customer); 
    
    boolean deleteCustomer(int customerId);
    
	Customers addCustomer2(Customers customer);
	
	int getCustomerIdByLicense(String licenseNumber) throws DatabaseConnectionException;
}
