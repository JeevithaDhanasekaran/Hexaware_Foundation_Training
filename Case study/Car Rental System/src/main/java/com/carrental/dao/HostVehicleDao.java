package com.carrental.dao;

import java.util.List;

import com.carrental.entity.HostVehicle;

public interface HostVehicleDao {
	boolean addHostVehicle(HostVehicle hostVehicle);

	boolean removeHostVehicle(int hostVehicleId);

	HostVehicle findByVehicleId(int vehicleId);

	List<HostVehicle> findAll();

	List<HostVehicle> findByHostCustomerId(int hostCustomerId);
}
