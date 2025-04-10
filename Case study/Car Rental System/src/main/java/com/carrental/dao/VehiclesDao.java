package com.carrental.dao;

import java.util.List;

import com.carrental.entity.Vehicles;

public interface VehiclesDao {
	// Insert new vehicle
		boolean addVehicle(Vehicles vehicle);

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
		boolean updateVehicleRating(int vehicleId, int givenRating); // givenRating = 1 to 5

}
