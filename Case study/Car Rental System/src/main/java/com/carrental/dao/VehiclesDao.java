package com.carrental.dao;

import java.math.BigDecimal;
import java.util.List;

import com.carrental.entity.Vehicles;
import com.carrental.exception.CarNotFoundException;
import com.carrental.exception.DatabaseConnectionException;

public interface VehiclesDao {
	// Insert new vehicle
	boolean addVehicle(Vehicles vehicle) throws CarNotFoundException;

	// Update existing vehicle details
	boolean updateVehicle(Vehicles vehicle);

	// Delete vehicle by ID
	boolean deleteVehicle(int vehicleId);

	// Get vehicle by ID
	Vehicles getVehicleById(int vehicleId);

	// Get all vehicles
	List<Vehicles> getAllVehicles();

	// Search by Make & Model
	List<Vehicles> searchVehiclesByMakeModel(String make, String model);

	// Update Rating - Special Method (Whenever customer gives rating)
	boolean updateVehicleRating(int vehicleId, double newRating); // givenRating = 1 to 5

	List<Vehicles> getVehiclesByCityId(int cityId);

	List<Vehicles> getAvailableVehicles() throws DatabaseConnectionException;

	int addVehicleAndReturnId(Vehicles vehicle);

	BigDecimal getTotalRevenue() throws DatabaseConnectionException;

}
