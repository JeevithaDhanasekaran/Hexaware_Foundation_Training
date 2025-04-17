package com.carrental.service.implementations;

import java.sql.Connection;
import java.util.List;

import com.carrental.dao.CustomersDao;
import com.carrental.dao.implementations.CustomersDaoImpl;
import com.carrental.entity.Customers;
import com.carrental.exception.CustomerNotFoundException;
import com.carrental.exception.DatabaseConnectionException;
import com.carrental.exception.InvalidInputException;
import com.carrental.service.CustomersService;
import com.carrental.util.DBconnection;

public class CustomersServiceImpl implements CustomersService {
	private CustomersDao customersDao;

	public CustomersServiceImpl(String fileName) throws DatabaseConnectionException {
		Connection connection = DBconnection.getConnection(fileName);
		customersDao = new CustomersDaoImpl(connection);
	}

	@Override
	public boolean addCustomer(Customers customer)
			throws InvalidInputException, DatabaseConnectionException, CustomerNotFoundException {
		if (customer == null) {
			throw new InvalidInputException("Customer Details Cannot Be Null");
		}
		return customersDao.addCustomer(customer);
	}

	@Override
	public Customers findCustomerById(int customerId) throws InvalidInputException {
		return customersDao.findById(customerId);
	}

	@Override
	public String getLicenseNumberByCustomerId(int customerId) {
		return customersDao.getLicenseNumberByCustomerId(customerId);
	}

	@Override
	public List<Customers> getAllCustomers() throws DatabaseConnectionException, InvalidInputException {
		return customersDao.findAllCustomers();
	}

	@Override
	public boolean updateCustomer(Customers customer) throws InvalidInputException {
		if (customer == null) {
			throw new InvalidInputException("Customer Details Cannot Be Null");
		}
		return customersDao.updateCustomer(customer);
	}

	@Override
	public boolean deleteCustomer(int customerId) {
		return customersDao.deleteCustomer(customerId);
	}

	@Override
	public Customers addCustomer2(Customers customer) throws InvalidInputException, DatabaseConnectionException {
		if (customer == null || customer.getFirstName() == null || customer.getLastName() == null
				|| customer.getLicenseNumber() == null) {
			throw new InvalidInputException("Customer details cannot be null");
		}

		if (!customer.getLicenseNumber().matches("^[A-Za-z]{2}\\d{7}$")) {
			throw new InvalidInputException("Invalid license number format.");
		}

		return customersDao.addCustomer2(customer);
	}

	@Override
	public int getCustomerIdByLicense(String licenseNumber) throws InvalidInputException, DatabaseConnectionException {
		if (licenseNumber == null || licenseNumber.isEmpty()) {
			throw new InvalidInputException("License number cannot be null or empty.");
		}
		return customersDao.getCustomerIdByLicense(licenseNumber);
	}

}
