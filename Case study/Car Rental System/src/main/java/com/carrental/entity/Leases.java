package com.carrental.entity;

import java.sql.Date;

import com.carrental.exception.InvalidInputException;

public class Leases {
	//encapsulation
	private int leaseId;
	private Vehicles vehicle;//tight coupling
	private Customers customer;//tight coupling
	private Date startDate;
	private Date endDate;
	private String leaseType;
	
	public Leases() {}

	public Leases(int leaseId, Vehicles vehicle, Customers customer, Date startDate, Date endDate, String leaseType) {
		super();
		this.leaseId = leaseId;
		this.vehicle = vehicle;
		this.customer = customer;
		this.startDate = startDate;
		this.endDate = endDate;
		this.leaseType = leaseType;
	}

	// Getters
	public int getLeaseId() {
		return leaseId;
	}

	public Vehicles getVehicle() {
		return vehicle;
	}

	public Customers getCustomer() {
		return customer;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String getLeaseType() {
		return leaseType;
	}

	// Setters
	public void setLeaseId(int leaseId) {
		this.leaseId = leaseId;
	}

	public void setVehicle(Vehicles vehicle) throws InvalidInputException {
		if (vehicle == null)
			throw new InvalidInputException("Vehicle must not be null");
		this.vehicle = vehicle;
	}

	public void setCustomer(Customers customer) throws InvalidInputException {
		if (customer == null)
			throw new InvalidInputException("Customer must not be null");
		this.customer = customer;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setLeaseType(String leaseType) throws InvalidInputException {
		if (leaseType == null || leaseType.trim().isEmpty()) {
            throw new InvalidInputException("Lease type cannot be empty.");
        }
        String trimmedType = leaseType.trim().toUpperCase();
        if (!trimmedType.equals("DAILY") && !trimmedType.equals("MONTHLY")) {
            throw new InvalidInputException("Invalid lease type. Allowed values are: 'daily' or 'monthly'.");
        }
        this.leaseType = trimmedType;
	}

	@Override
	public String toString() {
		return "Lease ID: " + leaseId + ", Vehicle ID: " + vehicle.getVehicleId() + ", Customer ID: "
				+ customer.getCustomerId() + ", Start Date: " + startDate + ", End Date: " + endDate + ", Lease Type: "
				+ leaseType;
	}

}