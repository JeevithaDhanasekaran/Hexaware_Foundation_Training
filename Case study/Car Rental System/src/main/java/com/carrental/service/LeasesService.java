package com.carrental.service;

import java.util.List;

import com.carrental.entity.Leases;

public interface LeasesService {
	Leases createLease(Leases lease);
    boolean returnVehicle(int leaseId);
    Leases findLeaseById(int leaseId);
    List<Leases> getAllLeases();
    List<Leases> getActiveLeases();
    List<Leases> getLeaseHistoryByCustomerId(int customerId);
}
