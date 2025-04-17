package com.hexaware.carrental.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hexaware.carrental.entity.CustomerInfo;
import com.hexaware.carrental.entity.HostCustomers;
import com.hexaware.carrental.entity.Vehicles;
import com.hexaware.carrental.exception.DatabaseConnectionException;
import com.hexaware.carrental.exception.InvalidInputException;

public interface HostCustomersService {

	// host customers services

	boolean addHostCustomer(HostCustomers hostCustomer) throws InvalidInputException;

	HostCustomers findByHostCustomerId(int hostCustomerId) throws InvalidInputException;

	HostCustomers findByCustomerId(int customerId) throws InvalidInputException;

	List<HostCustomers> getAllHostCustomers();

	List<HostCustomers> findByCityId(int cityId);

	boolean updateHostCustomer(HostCustomers hostCustomer) throws InvalidInputException;

	boolean deleteHostCustomer(int customerId);

	// maps to HostCustomers with the CustomerInfo details

	Map<HostCustomers, CustomerInfo> getAllHostCustomersWithInfo()
			throws DatabaseConnectionException, InvalidInputException;

	// ----------------------------------------------------
	// host vehicle service (map host to vehicles )
	// -----------------------------------------------------

	boolean addHostVehicle(int hostCustomerId, int vehicleId) throws InvalidInputException;

	List<Vehicles> findVehiclesByHostCustomerId(int hostCustomerId)
			throws DatabaseConnectionException, InvalidInputException;

	// no update since in real time either they host or delete,or add new.Not
	// replace a car with a new one.

	boolean deleteHostVehicle(int vehicleId);

	int countVehiclesByHostCustomerId(int hostCustomerId);

	BigDecimal getRevenueByHostId(int hostCustomerId) throws InvalidInputException, DatabaseConnectionException;

}
