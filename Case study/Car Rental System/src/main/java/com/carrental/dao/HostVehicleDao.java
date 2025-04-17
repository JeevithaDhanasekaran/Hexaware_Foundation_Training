package com.carrental.dao;

import java.util.List;

import com.carrental.entity.HostVehicle;
import com.carrental.entity.Vehicles;
import com.carrental.exception.InvalidInputException;

public interface HostVehicleDao {
	boolean addHostVehicle(int hostCustomerId, int vehicleId);

	boolean removeHostVehicle(int hostVehicleId);

	boolean deleteHostVehicle(int vehicleID);

	HostVehicle findByVehicleId(int vehicleId);

	List<HostVehicle> findAll();

	int countVehiclesByHostCustomerId(int hostCustomerId);

	List<Vehicles> findVehiclesByHostCustomerId(int hostCustomerId) throws InvalidInputException;
}
