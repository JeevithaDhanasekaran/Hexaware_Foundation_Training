package com.hexaware.carrental.service;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import com.hexaware.carrental.entity.Leases;
import com.hexaware.carrental.exception.DatabaseConnectionException;
import com.hexaware.carrental.exception.InvalidInputException;
import com.hexaware.carrental.exception.LeaseNotFoundException;
import com.hexaware.carrental.exception.PaymentRequiredException;

public interface LeasesService {

	boolean createLease(int customerId, int vehicleId, Date startDate, Date endDate, String leaseType)
			throws InvalidInputException, DatabaseConnectionException, LeaseNotFoundException;

	// return vehicle
	boolean returnVehicle(int leaseId)
			throws DatabaseConnectionException, InvalidInputException, PaymentRequiredException;

	Leases getLeaseById(int leaseId) throws DatabaseConnectionException;

	Set<Leases> getLeaseByCustomerId(int customerId);

	LinkedHashSet<Leases> getAllLeases() throws DatabaseConnectionException;

	LinkedHashSet<Leases> getActiveLeases() throws DatabaseConnectionException;

	Set<Leases> getLeaseHistoryByCustomer(int customerId);

	Set<Leases> getCompletedLeases();
}
