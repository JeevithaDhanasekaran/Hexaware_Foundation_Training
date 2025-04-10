package com.carrental.dao;

import java.util.List;

import com.carrental.entity.VehicleCity;

public interface VehicleCityDao {
	boolean addVehicleCity(VehicleCity vehicleCity);

	boolean removeVehicleCity(int vehicleCityId);

	VehicleCity findByVehicleId(int vehicleId);

	List<VehicleCity> findAll();

	List<VehicleCity> findByCityId(int cityId);
}
