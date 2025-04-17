package com.hexaware.carrental.dao;

import java.util.List;

import com.hexaware.carrental.entity.HostVehicle;
import com.hexaware.carrental.entity.Vehicles;
import com.hexaware.carrental.exception.InvalidInputException;

public interface HostVehicleDao {
	boolean addHostVehicle(int hostCustomerId, int vehicleId);

	boolean removeHostVehicle(int hostVehicleId);

	boolean deleteHostVehicle(int vehicleID);

	HostVehicle findByVehicleId(int vehicleId);

	List<HostVehicle> findAll();

	int countVehiclesByHostCustomerId(int hostCustomerId);

	List<Vehicles> findVehiclesByHostCustomerId(int hostCustomerId) throws InvalidInputException;
}
