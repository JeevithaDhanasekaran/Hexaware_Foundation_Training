package com.carrental.service;

import java.util.List;

import com.carrental.entity.Vehicles;

public interface VehicleCityService {
	boolean assignVehicleToCity(int vehicleId, int cityId);
    List<Vehicles> findVehiclesByCityId(int cityId);
}
