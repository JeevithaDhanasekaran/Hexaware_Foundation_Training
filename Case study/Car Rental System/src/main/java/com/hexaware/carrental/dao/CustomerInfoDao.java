package com.hexaware.carrental.dao;

import java.util.List;

import com.hexaware.carrental.entity.CustomerInfo;
import com.hexaware.carrental.exception.InvalidInputException;

public interface CustomerInfoDao {
	boolean addCustomerInfo(CustomerInfo customerInfo);
	
    CustomerInfo findByCustomerId(int customerId) throws InvalidInputException;//to find by customer id
    
    List<CustomerInfo> findAll() throws InvalidInputException;
    
    boolean updateCustomerInfo(CustomerInfo customerInfo);
    
    boolean deleteByContactId(int contactId);//delete by contact id
    
    boolean deleteByCustomerId(int customerId);//delete by customerID
    
    CustomerInfo findByContactId(int contactId) throws InvalidInputException; //to find by contact ID
    
	CustomerInfo findByPhone(String phone);
}
