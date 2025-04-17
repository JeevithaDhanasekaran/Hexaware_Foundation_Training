package com.carrental.service.implementations;

import java.sql.Connection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.carrental.dao.CustomersDao;
import com.carrental.dao.LeasesDao;
import com.carrental.dao.PaymentsDao;
import com.carrental.dao.VehiclesDao;
import com.carrental.dao.implementations.CustomersDaoImpl;
import com.carrental.dao.implementations.LeasesDaoImpl;
import com.carrental.dao.implementations.PaymentsDaoImpl;
import com.carrental.dao.implementations.VehiclesDaoImpl;
import com.carrental.entity.Customers;
import com.carrental.entity.Leases;
import com.carrental.entity.Vehicles;
import com.carrental.exception.DatabaseConnectionException;
import com.carrental.exception.InvalidInputException;
import com.carrental.exception.LeaseNotFoundException;
import com.carrental.exception.PaymentRequiredException;
import com.carrental.service.LeasesService;
import com.carrental.util.DBconnection;

public class LeasesServiceImpl implements LeasesService {

	private LeasesDao leasesDao;
	private VehiclesDao vehiclesDao;
	private CustomersDao customersDao;
	private PaymentsDao paymentsDao;

	public LeasesServiceImpl(String fileName) throws DatabaseConnectionException {
		Connection connection = DBconnection.getConnection(fileName);
		leasesDao = new LeasesDaoImpl(connection);
		vehiclesDao = new VehiclesDaoImpl(connection);
		customersDao = new CustomersDaoImpl(connection);
		paymentsDao = new PaymentsDaoImpl(connection);// to ensure payment handling during returning a vehicle
	}

	@Override
	public boolean createLease(int customerId, int vehicleId, Date startDate, Date endDate, String leaseType)
			throws InvalidInputException, LeaseNotFoundException {
		if (startDate == null || endDate == null || leaseType == null)
			throw new InvalidInputException("Lease details cannot be null");

		// fetch vehicle and customer object
		Vehicles vehicle = vehiclesDao.getVehicleById(vehicleId);
		if (vehicle == null) {
			throw new InvalidInputException("Vehicle not found with ID: " + vehicleId);
		}

		Customers customer = customersDao.findById(customerId);
		if (customer == null) {
			throw new InvalidInputException("Customer not found with ID: " + customerId);
		}

		Leases lease = new Leases();
		lease.setVehicle(vehicle);
		lease.setCustomer(customer);
		lease.setStartDate(new java.sql.Date(startDate.getTime()));// java.sql.Date uses the milliseconds
		lease.setEndDate(new java.sql.Date(endDate.getTime()));// returns long value representing the number of
																// milliseconds from January 1, 1970 00:00:00 GMT
		lease.setLeaseType(leaseType);

		return leasesDao.createLease(lease);
	}

	@Override
	public boolean returnVehicle(int leaseId)
			throws DatabaseConnectionException, InvalidInputException, PaymentRequiredException {
		if (leaseId <= 0) {
			throw new InvalidInputException("Invalid leaseId");
		}

		Leases lease = leasesDao.findById(leaseId);
		if (lease == null) {
			throw new InvalidInputException("Lease not found with ID: " + leaseId);
		}

//        Date today = new Date();
//        if (lease.getEndDate().before(today)) {
//            throw new InvalidInputException("Lease is already closed.");
//        }

		boolean paymentMade = paymentsDao.isPaymentMadeForLease(leaseId);

		if (!paymentMade) {
			throw new PaymentRequiredException("Payment is required before returning the vehicle.");
		}

		return leasesDao.closeLease(leaseId);
	}

	@Override
	public Leases getLeaseById(int leaseId) throws DatabaseConnectionException {
		return leasesDao.findById(leaseId);
	}

	@Override
	public LinkedHashSet<Leases> getAllLeases() throws DatabaseConnectionException {
		return new LinkedHashSet<>(leasesDao.findAllLeases());// no duplicates + maintain insertion order
	}

	@Override
	public LinkedHashSet<Leases> getActiveLeases() throws DatabaseConnectionException {
		return new LinkedHashSet<>(leasesDao.findActiveLeases());// order + no duplicates
	}

	@Override
	public Set<Leases> getLeaseHistoryByCustomer(int customerId) {
		return new HashSet<>(leasesDao.findLeaseHistoryByCustomer(customerId));
	}

	@Override
	public Set<Leases> getCompletedLeases() {
		return new HashSet<>(leasesDao.findAllCompletedLeases());
	}

	@Override
	public Set<Leases> getLeaseByCustomerId(int customerId) {
		return new HashSet<>(leasesDao.getLeasesByCustomerID(customerId));
	}

}
