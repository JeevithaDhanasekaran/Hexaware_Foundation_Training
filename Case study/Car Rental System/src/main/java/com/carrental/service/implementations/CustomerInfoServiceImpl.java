package com.carrental.service.implementations;

import java.sql.Connection;
import java.util.List;

import com.carrental.dao.CustomerInfoDao;
import com.carrental.dao.implementations.CustomerInfoDaoImpl;
import com.carrental.entity.CustomerInfo;
import com.carrental.exception.DatabaseConnectionException;
import com.carrental.exception.InvalidInputException;
import com.carrental.service.CustomerInfoService;
import com.carrental.util.DBconnection;

public class CustomerInfoServiceImpl implements CustomerInfoService {

	private CustomerInfoDao customerInfoDao;

	public CustomerInfoServiceImpl(String fileName) throws DatabaseConnectionException {
		Connection connection = DBconnection.getConnection(fileName);
		customerInfoDao = new CustomerInfoDaoImpl(connection);
	}

	@Override
	public boolean addCustomerInfo(CustomerInfo customerInfo)
			throws InvalidInputException, DatabaseConnectionException {
		if (customerInfo == null) {
			throw new InvalidInputException("Customer Info Cannot Be Null");
		}
		return customerInfoDao.addCustomerInfo(customerInfo);
	}

	@Override
	public CustomerInfo findByCustomerId(int customerId) throws InvalidInputException {
		return customerInfoDao.findByCustomerId(customerId);

	}

	@Override
	public List<CustomerInfo> findAll() throws DatabaseConnectionException, InvalidInputException {
		return customerInfoDao.findAll();
	}

	@Override
	public boolean updateCustomerInfo(CustomerInfo customerInfo)
			throws InvalidInputException, DatabaseConnectionException {
		if (customerInfo == null) {
			throw new InvalidInputException("Customer Info Cannot Be Null");
		}
		return customerInfoDao.updateCustomerInfo(customerInfo);
	}

	@Override
	public boolean deleteByCustomerId(int customerId) throws DatabaseConnectionException {
		return customerInfoDao.deleteByCustomerId(customerId);
	}

	@Override
	public CustomerInfo findCustomerIdByPhone(String phone) {
		return customerInfoDao.findByPhone(phone);
	}

}
