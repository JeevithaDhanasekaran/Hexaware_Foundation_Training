package com.carrental.service;

import java.util.List;

import com.carrental.entity.Vehicles;

public interface HostVehicleService {
	boolean addHostVehicle(int hostCustomerId, int vehicleId);
    List<Vehicles> findVehiclesByHostCustomerId(int hostCustomerId);
    boolean deleteHostVehicle(int vehicleId);
}
