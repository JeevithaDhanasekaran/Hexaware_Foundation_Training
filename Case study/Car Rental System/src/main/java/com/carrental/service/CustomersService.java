package com.carrental.service;

import java.util.List;

import com.carrental.entity.Customers;

public interface CustomersService {
	Customers addCustomer(Customers customer);
    Customers findCustomerById(int customerId);
    String getLicenseNumberByCustomerId(int customerId);
    List<Customers> getAllCustomers();
    boolean updateCustomer(Customers customer);
    boolean deleteCustomer(int customerId);
}
