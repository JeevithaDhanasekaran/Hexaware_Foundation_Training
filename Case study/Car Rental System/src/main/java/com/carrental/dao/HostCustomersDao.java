package com.carrental.dao;

import java.util.List;

import com.carrental.entity.HostCustomers;
import com.carrental.exception.InvalidInputException;

public interface HostCustomersDao {
	boolean addHostCustomer(HostCustomers hostCustomer);
    HostCustomers findById(int hostCustomerId) throws InvalidInputException;
    HostCustomers findByCustomerId(int customerId) throws InvalidInputException;
    List<HostCustomers> findAll();
    List<HostCustomers> findByCityId(int cityId);
    boolean updateHostCustomer(HostCustomers hostCustomer);
    boolean deleteByHostCustomer(int hostCustomerId);
    boolean deleteByCustomerId(int customerId);
}
