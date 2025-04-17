package com.carrental.service.implementations;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.carrental.dao.CarStatusDao;
import com.carrental.dao.HostVehicleDao;
import com.carrental.dao.VehicleCityDao;
import com.carrental.dao.VehiclesDao;
import com.carrental.dao.implementations.CarStatusDaoImpl;
import com.carrental.dao.implementations.HostVehicleDaoImpl;
import com.carrental.dao.implementations.VehicleCityDaoImpl;
import com.carrental.dao.implementations.VehiclesDaoImpl;
import com.carrental.entity.CarStatus;
import com.carrental.entity.Vehicles;
import com.carrental.exception.CarNotFoundException;
import com.carrental.exception.DatabaseConnectionException;
import com.carrental.exception.InvalidInputException;
import com.carrental.service.VehiclesService;
import com.carrental.util.DBconnection;

public class VehiclesServiceImpl implements VehiclesService {

	private VehiclesDao vehiclesDao;
	private VehicleCityDao vehicleCityDao;
	private CarStatusDao carStatusDao;
	private HostVehicleDao hostVehicleDao;

	public VehiclesServiceImpl(String fileName) throws DatabaseConnectionException {
		Connection connection = DBconnection.getConnection(fileName);
		this.vehiclesDao = new VehiclesDaoImpl(connection);
		this.vehicleCityDao = new VehicleCityDaoImpl(connection);
		this.carStatusDao = new CarStatusDaoImpl(connection);
		hostVehicleDao = new HostVehicleDaoImpl(connection);
	}

	// vehicle - Service implementations

	@Override
	public boolean addVehicle(Vehicles vehicle) throws InvalidInputException, CarNotFoundException {
		if (vehicle == null) {
			throw new InvalidInputException("Vehicle Cannot Be Null");
		}
		int vehicleId = vehiclesDao.addVehicleAndReturnId(vehicle);
		if (vehicleId > 0) {
			vehicle.setVehicleId(vehicleId);
			return true;
		}
		return false;
	}

	@Override
	public Vehicles findVehicleById(int vehicleId) {
		return vehiclesDao.getVehicleById(vehicleId);
	}

	@Override
	public List<Vehicles> getAllVehicles() {
		return vehiclesDao.getAllVehicles();
	}

	@Override
	public List<Vehicles> getVehiclesByCity(int cityId) {
		return vehiclesDao.getVehiclesByCityId(cityId);
	}

	@Override
	public List<Vehicles> getVehiclesSortedByModel(int cityId) {
		List<Vehicles> list = vehiclesDao.getVehiclesByCityId(cityId);
		list.sort(Comparator.comparing(Vehicles::getModel));// comparator to sort using model
		return list;
	}

	@Override
	public List<Vehicles> getVehiclesSortedByRating(int cityId) {
		List<Vehicles> list = vehiclesDao.getVehiclesByCityId(cityId);
		Collections.sort(list, new Comparator<Vehicles>() {
			@Override
			public int compare(Vehicles v1, Vehicles v2) {
				return Double.compare(v2.getRating(), v1.getRating()); // descending //traditional equivalent approach
			}
		});
		return list;
	}

	@Override
	public List<Vehicles> getVehiclesSortedByPassengerCapacity(int cityId) {
		List<Vehicles> list = vehiclesDao.getVehiclesByCityId(cityId);
		list.sort(Comparator.comparing(Vehicles::getPassengerCapacity).reversed());
		return list;
	}

	@Override
	public boolean updateVehicle(Vehicles vehicle) throws InvalidInputException {
		if (vehicle == null) {
			throw new InvalidInputException("Vehicle Cannot Be Null");
		}
		return vehiclesDao.updateVehicle(vehicle);
	}

	@Override
	public boolean deleteVehicle(int vehicleId) {
		try {
			// Delete from HostVehicle
			hostVehicleDao.removeHostVehicle(vehicleId);

			// Delete from VehicleCity
			vehicleCityDao.removeVehicleCity(vehicleId);

			// Delete from CarStatus
			carStatusDao.deleteCarStatus(vehicleId);

			// Delete from Vehicles
			return vehiclesDao.deleteVehicle(vehicleId);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error removing vehicle" + e.getMessage());
		}
		return false;
	}

	@Override
	public boolean updateVehicleRating(int vehicleId, double newRating)
			throws InvalidInputException, DatabaseConnectionException {
		if (newRating < 0 || newRating > 5) {
			throw new InvalidInputException("Rating Must Be Between 0 and 5");
		}
		return vehiclesDao.updateVehicleRating(vehicleId, newRating);
	}

	@Override
	public BigDecimal getTotalRevenue() throws DatabaseConnectionException {
		return vehiclesDao.getTotalRevenue();
	}

//Vehicle City Service implementation

	@Override
	public boolean addVehicleToCity(Vehicles vehicle, int cityId) throws InvalidInputException {

		// ternary -replace if-else
		return vehicleCityDao.addVehicleCity(vehicle.getVehicleId(), cityId);
	}

//CarStatus Service implementation
	@Override
	public boolean addCarStatus(int vehicleId, String status) throws InvalidInputException {
		// since carStatus has no constructor like vehicleID,status ->

		CarStatus carStatus = new CarStatus();
		Vehicles vehicle = new Vehicles();
		vehicle.setVehicleId(vehicleId);
		carStatus.setVehicle(vehicle);
		carStatus.setStatus(status);

		return carStatusDao.addCarStatus(carStatus);
	}

	@Override
	public boolean updateCarStatus(int vehicleId, String newStatus) throws InvalidInputException {
		// since carStatus has no constructor like vehicleID,status ->

		CarStatus carStatus = new CarStatus();
		Vehicles vehicle = new Vehicles();
		vehicle.setVehicleId(vehicleId);
		carStatus.setVehicle(vehicle);
		carStatus.setStatus(newStatus);

		return carStatusDao.updateCarStatus(carStatus);
	}

	@Override
	public CarStatus getCarStatusByVehicleId(int vehicleId) {
		return carStatusDao.findByVehicleId(vehicleId);
	}

	@Override
	public List<CarStatus> getAllCarStatus() {
		List<CarStatus> carStatuses = carStatusDao.findAll();
		// Using LinkedHashSet to remove duplicates if any & maintain order
		Set<CarStatus> statusSet = new LinkedHashSet<>(carStatuses);
		return new ArrayList<>(statusSet);
	}

	@Override
	public boolean deleteCarStatus(int vehicleStatusId) {
		return carStatusDao.deleteCarStatus(vehicleStatusId);
	}

	@Override
	public boolean addHostCustomerToVehicle(int hostCustomerId, int vehicleId) {
		return hostVehicleDao.addHostVehicle(hostCustomerId, vehicleId);
	}

	@Override
	public boolean updateVehicleCity(int vehicleId, int cityId) {
		return vehicleCityDao.updateVehicleCity(vehicleId, cityId);

	}

}
