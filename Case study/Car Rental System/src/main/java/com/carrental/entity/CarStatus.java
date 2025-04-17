package com.carrental.entity;

import com.carrental.exception.InvalidInputException;

public class CarStatus {
	private int vehicleStatusId;
	private Vehicles vehicle;  //Composition
	private String status;     //AVAILABLE/NOT_AVAILABLE /MAINTENANCE /BOOKED
	
	//no argument constructor
	public CarStatus() {
	}

	//parameterized constructor
	public CarStatus(int vehicleStatusId, Vehicles vehicle, String status) {
		this.vehicleStatusId = vehicleStatusId;
		this.vehicle = vehicle;
		this.status = status;
	}
	
	//getters and setters
	public int getVehicleStatusId() {
		return vehicleStatusId;
	}

	public void setVehicleStatusId(int vehicleStatusId) {
		this.vehicleStatusId = vehicleStatusId;
	}

	public Vehicles getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicles vehicle) throws InvalidInputException {
		if (vehicle == null)
			throw new InvalidInputException("Vehicle must not be null for Car Status");
		this.vehicle = vehicle;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) throws InvalidInputException {
		if (status == null || !(status.equalsIgnoreCase("AVAILABLE") || status.equalsIgnoreCase("NOT_AVAILABLE")
				|| status.equalsIgnoreCase("MAINTENANCE") || status.equalsIgnoreCase("BOOKED"))) {
			throw new InvalidInputException("Invalid Status! Allowed: AVAILABLE, NOT_AVAILABLE, MAINTENANCE, BOOKED");
		}
		this.status = status.toUpperCase();
	}

	@Override
	public String toString() {
		return String.format("CarStatus { ID=" + vehicleStatusId + ", Vehicle ID=" + vehicle.getVehicleId() + ", Status=" + status
				+ " }");
	}
}
