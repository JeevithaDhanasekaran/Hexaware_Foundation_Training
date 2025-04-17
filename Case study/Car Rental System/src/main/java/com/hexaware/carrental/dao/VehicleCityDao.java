package com.hexaware.carrental.dao;

import java.util.List;

import com.hexaware.carrental.entity.VehicleCity;

public interface VehicleCityDao {
	boolean addVehicleCity(int vehicleId, int cityId);

	boolean removeVehicleCity(int vehicleCityId);

	VehicleCity findByVehicleId(int vehicleId);

	List<VehicleCity> findAll();

	List<VehicleCity> findByCityId(int cityId);

	boolean updateVehicleCity(int vehicleId, int cityId);
}
