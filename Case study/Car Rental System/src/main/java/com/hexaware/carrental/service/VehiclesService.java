package com.hexaware.carrental.service;

import java.math.BigDecimal;
import java.util.List;

import com.hexaware.carrental.entity.CarStatus;
import com.hexaware.carrental.entity.Vehicles;
import com.hexaware.carrental.exception.CarNotFoundException;
import com.hexaware.carrental.exception.DatabaseConnectionException;
import com.hexaware.carrental.exception.InvalidInputException;

public interface VehiclesService {
	boolean addVehicle(Vehicles vehicle) throws InvalidInputException, CarNotFoundException;

	Vehicles findVehicleById(int vehicleId);

	List<Vehicles> getAllVehicles();

	List<Vehicles> getVehiclesByCity(int cityId);

	List<Vehicles> getVehiclesSortedByModel(int cityId);

	List<Vehicles> getVehiclesSortedByRating(int cityId);

	List<Vehicles> getVehiclesSortedByPassengerCapacity(int cityId);
	
	int getVehicleIdByLeaseId(int leaseId) throws DatabaseConnectionException;

	boolean updateVehicle(Vehicles vehicle) throws InvalidInputException;

	boolean deleteVehicle(int vehicleId);

	public boolean updateVehicleRating(int vehicleId, double newRating)
			throws InvalidInputException, DatabaseConnectionException;

	// vehicle city service

	boolean addVehicleToCity(Vehicles vehicle, int cityId) throws InvalidInputException;

	// Car status service

	boolean addCarStatus(int vehicleId, String status) throws InvalidInputException;

	boolean updateCarStatus(int vehicleId, String newStatus) throws InvalidInputException;

	CarStatus getCarStatusByVehicleId(int vehicleId);

	List<CarStatus> getAllCarStatus();

	boolean deleteCarStatus(int vehicleStatusId);

	boolean addHostCustomerToVehicle(int hostCustomerId, int vehicleId);

	boolean updateVehicleCity(int vehicleId, int cityId);

	BigDecimal getTotalRevenue() throws DatabaseConnectionException;

	List<Vehicles> getAvailableVehicles() throws DatabaseConnectionException;

//    VehiclesService
//    → Add Vehicle
//    → Update Vehicle
//    → Delete Vehicle
//    → Search Vehicles
//    → Add/Update Car Status  ← Status Handling
//    → Add/Update Vehicle City ← City Mapping

}
