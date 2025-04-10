package com.carrental.service;

import java.util.List;

import com.carrental.entity.Vehicles;

public interface VehiclesService {
	Vehicles addVehicle(Vehicles vehicle);
    Vehicles findVehicleById(int vehicleId);
    List<Vehicles> getAllVehicles();
    List<Vehicles> getVehiclesByCity(int cityId);
    List<Vehicles> getVehiclesSortedByModel(int cityId);
    List<Vehicles> getVehiclesSortedByRating(int cityId);
    List<Vehicles> getVehiclesSortedByPassengerCapacity(int cityId);
    boolean updateVehicle(Vehicles vehicle);
    boolean deleteVehicle(int vehicleId);
}
