package com.carrental.service;

import java.util.List;

import com.carrental.entity.CarStatus;

public interface CarStatusService {
	boolean addCarStatus(CarStatus carStatus);
    boolean updateCarStatus(CarStatus carStatus);
    CarStatus findByVehicleId(int vehicleId);
    List<CarStatus> getAllCarStatus();
    boolean deleteCarStatus(int vehicleStatusId);
}
