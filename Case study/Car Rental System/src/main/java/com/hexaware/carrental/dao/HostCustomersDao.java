package com.hexaware.carrental.dao;

import java.math.BigDecimal;
import java.util.List;

import com.hexaware.carrental.entity.HostCustomers;
import com.hexaware.carrental.exception.DatabaseConnectionException;
import com.hexaware.carrental.exception.InvalidInputException;

public interface HostCustomersDao {
	boolean addHostCustomer(HostCustomers hostCustomer);

	HostCustomers findById(int hostCustomerId) throws InvalidInputException;

	HostCustomers findByCustomerId(int customerId) throws InvalidInputException;

	List<HostCustomers> findAll();

	List<HostCustomers> findByCityId(int cityId);

	boolean updateHostCustomer(HostCustomers hostCustomer);

	boolean deleteByHostCustomer(int hostCustomerId);

	boolean deleteByCustomerId(int customerId);

	BigDecimal getRevenueByHostId(int hostCustomerId) throws DatabaseConnectionException;
}
