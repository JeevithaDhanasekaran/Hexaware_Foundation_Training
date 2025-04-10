package com.carrental.service;

import java.util.List;

import com.carrental.entity.HostCustomers;

public interface HostCustomersService {
	HostCustomers addHostCustomer(HostCustomers hostCustomer);
    HostCustomers findByHostCustomerId(int hostCustomerId);
    HostCustomers findByCustomerId(int customerId);
    List<HostCustomers> getAllHostCustomers();
    List<HostCustomers> findByCityId(int cityId);
    boolean updateHostCustomer(HostCustomers hostCustomer);
    boolean deleteHostCustomer(int customerId);
}
