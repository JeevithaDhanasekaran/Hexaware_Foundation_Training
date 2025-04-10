package com.carrental.dao;

import java.util.List;

import com.carrental.entity.Leases;

public interface LeasesDao {

	boolean createLease(Leases lease);

	Leases findById(int leaseId);

	List<Leases> findAllLeases();

	List<Leases> findActiveLeases();
	
	//method to find the inactive leases that is completed

	List<Leases> findLeaseHistoryByCustomer(int customerId);

	boolean updateLease(Leases lease);

	boolean deleteLease(int leaseId);

}
