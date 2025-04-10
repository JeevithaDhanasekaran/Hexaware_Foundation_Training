package com.carrental.dao;

import java.util.List;

import com.carrental.entity.CarStatus;

public interface CarStatusDao {
	
	boolean addCarStatus(CarStatus carStatus);

	boolean updateCarStatus(CarStatus carStatus);

	boolean deleteCarStatus(int vehicleStatusId);

	CarStatus findByVehicleId(int vehicleId);

	List<CarStatus> findAll();
}
