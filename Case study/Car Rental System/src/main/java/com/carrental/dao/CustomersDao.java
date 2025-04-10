package com.carrental.dao;

import java.util.List;

import com.carrental.entity.Customers;
import com.carrental.exception.DatabaseConnectionException;
import com.carrental.exception.InvalidInputException;

public interface CustomersDao {
	boolean addCustomer(Customers customer) throws DatabaseConnectionException;
    Customers findById(int customerId) throws InvalidInputException;
    String getLicenseNumberByCustomerId(int customerId);//get the license number by customer ID
    List<Customers> findAllCustomers() throws InvalidInputException;//list all customers irrespective of roles
    boolean updateCustomer(Customers customer); 
    boolean deleteCustomer(int customerId);
}
