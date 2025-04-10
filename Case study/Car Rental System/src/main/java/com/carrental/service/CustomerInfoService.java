package com.carrental.service;

import java.util.List;

import com.carrental.entity.CustomerInfo;

public interface CustomerInfoService {
	CustomerInfo addCustomerInfo(CustomerInfo customerInfo);
    CustomerInfo findByCustomerId(int customerId);
    List<CustomerInfo> getAllCustomerInfo();
    boolean updateCustomerInfo(CustomerInfo customerInfo);
    boolean deleteByCustomerId(int customerId);
}
