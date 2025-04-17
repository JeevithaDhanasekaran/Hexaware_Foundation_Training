package com.carrental.service;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import com.carrental.entity.Leases;
import com.carrental.exception.DatabaseConnectionException;
import com.carrental.exception.InvalidInputException;
import com.carrental.exception.LeaseNotFoundException;
import com.carrental.exception.PaymentRequiredException;

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
