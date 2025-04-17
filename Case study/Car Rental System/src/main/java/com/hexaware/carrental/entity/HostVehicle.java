package com.hexaware.carrental.entity;

import com.hexaware.carrental.exception.InvalidInputException;

public class HostVehicle {
	
	//encapsulation
	private int hostVehicleId;
	private HostCustomers hostCustomers;//composition
	private Vehicles vehicle;//composition

	//no argument constructor
	public HostVehicle() {
	}

	//parameterized constructor
	public HostVehicle(int hostVehicleId, HostCustomers hostCustomers, Vehicles vehicle) {
		this.hostVehicleId = hostVehicleId;
		this.hostCustomers = hostCustomers;
		this.vehicle = vehicle;
	}

	//getters and setters 
	public int getHostVehicleId() {
		return hostVehicleId;
	}

	public void setHostVehicleId(int hostVehicleId) {
		this.hostVehicleId = hostVehicleId;
	}

	public HostCustomers getHostCustomers() {
		return hostCustomers;
	}

	public void setHostCustomers(HostCustomers hostCustomers) throws InvalidInputException {
		if (hostCustomers == null)
			throw new InvalidInputException("HostCustomer cannot be null");
		this.hostCustomers = hostCustomers;
	}

	public Vehicles getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicles vehicle) throws InvalidInputException {
		if (vehicle == null)
			throw new InvalidInputException("Vehicle cannot be null");
		this.vehicle = vehicle;
	}

	@Override
	public String toString() {
		return String.format("HostVehicle { ID= " + hostVehicleId + ", HostCustomerID= " + hostCustomers.getHostCustomerId()
				+ ", VehicleID= " + vehicle.getVehicleId() + " }");
	}
}
