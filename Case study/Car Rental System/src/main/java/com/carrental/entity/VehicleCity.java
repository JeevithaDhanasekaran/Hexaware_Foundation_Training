package com.carrental.entity;

import com.carrental.exception.InvalidInputException;

public class VehicleCity {
	private int vehicleCityId;
	private Vehicles vehicle;
	private City city;

	public VehicleCity() {}

	public VehicleCity(int vehicleCityId, Vehicles vehicle, City city) {
		this.vehicleCityId = vehicleCityId;
		this.vehicle = vehicle;
		this.city = city;
	}
	
	public int getVehicleCityId() {
		return vehicleCityId;
	}

	public void setVehicleCityId(int vehicleCityId) {
		this.vehicleCityId = vehicleCityId;
	}

	public Vehicles getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicles vehicle) throws InvalidInputException {
		if(vehicle == null)
			throw new InvalidInputException("Vehicle Details must not be null");
		this.vehicle = vehicle;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) throws InvalidInputException {
		if(city == null)
			throw new InvalidInputException("City Details must not be null");
		this.city = city;
	}

	@Override
	public String toString() {
		return "VehicleCity { vehicleCityId=" + vehicleCityId +
			   ", vehicleID=" + vehicle.getVehicleId() +
			   ", cityID=" + city.getCityId() + " }";
	}
	
}
