package com.carrental.dao;

import java.util.List;

import com.carrental.entity.Leases;
import com.carrental.exception.DatabaseConnectionException;
import com.carrental.exception.LeaseNotFoundException;

public interface LeasesDao {

	boolean createLease(Leases lease) throws LeaseNotFoundException;

	Leases findById(int leaseId);

	List<Leases> findAllLeases();

	List<Leases> findActiveLeases();

	// method to find the inactive leases that is completed by customer id

	List<Leases> findLeaseHistoryByCustomer(int customerId);

	boolean updateLease(Leases lease);

	// return the vehicle -> closing the lease
	boolean closeLease(int leaseId) throws DatabaseConnectionException;

	boolean deleteLease(int leaseId);

	// method to find all inactive leases that is completed

	List<Leases> findAllCompletedLeases();

	List<Leases> getLeasesByCustomerID(int customerId);

}
