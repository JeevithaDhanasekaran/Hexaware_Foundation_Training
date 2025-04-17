package com.hexaware.carrental.dao;

import java.util.List;

import com.hexaware.carrental.entity.CarStatus;

public interface CarStatusDao {
	
	boolean addCarStatus(CarStatus carStatus);

	boolean updateCarStatus(int vehicleId,String newStatus);

	boolean deleteCarStatus(int vehicleStatusId);

	CarStatus findByVehicleId(int vehicleId);

	List<CarStatus> findAll();
}
