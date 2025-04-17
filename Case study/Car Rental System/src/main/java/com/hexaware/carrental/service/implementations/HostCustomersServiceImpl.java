package com.hexaware.carrental.service.implementations;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hexaware.carrental.dao.HostCustomersDao;
import com.hexaware.carrental.dao.HostVehicleDao;
import com.hexaware.carrental.dao.VehiclesDao;
import com.hexaware.carrental.dao.implementations.HostCustomersDaoImpl;
import com.hexaware.carrental.dao.implementations.HostVehicleDaoImpl;
import com.hexaware.carrental.dao.implementations.VehiclesDaoImpl;
import com.hexaware.carrental.entity.CustomerInfo;
import com.hexaware.carrental.entity.HostCustomers;
import com.hexaware.carrental.entity.Vehicles;
import com.hexaware.carrental.exception.DatabaseConnectionException;
import com.hexaware.carrental.exception.InvalidInputException;
import com.hexaware.carrental.service.CustomerInfoService;
import com.hexaware.carrental.service.HostCustomersService;
import com.hexaware.carrental.util.DBconnection;

public class HostCustomersServiceImpl implements HostCustomersService {

	private HostCustomersDao hostCustomersDao;
	private CustomerInfoService customerInfoService;
	private HostVehicleDao hostVehicleDao;
	private VehiclesDao vehiclesDao;

	public HostCustomersServiceImpl(String filename) throws DatabaseConnectionException {
		Connection connection = DBconnection.getConnection(filename);
		hostCustomersDao = new HostCustomersDaoImpl(connection);
		this.hostVehicleDao = new HostVehicleDaoImpl(connection);
		this.vehiclesDao = new VehiclesDaoImpl(connection);
		customerInfoService = new CustomerInfoServiceImpl(filename);
	}

	@Override
	public boolean addHostCustomer(HostCustomers hostCustomer) throws InvalidInputException {
		if (hostCustomer == null) {
			throw new InvalidInputException("Host Customer Cannot Be Null");
		}
		return hostCustomersDao.addHostCustomer(hostCustomer);
	}

	@Override
	public HostCustomers findByHostCustomerId(int hostCustomerId) throws InvalidInputException {
		return hostCustomersDao.findById(hostCustomerId);
	}

	@Override
	public HostCustomers findByCustomerId(int customerId) throws InvalidInputException {
		return hostCustomersDao.findByCustomerId(customerId);
	}

	@Override
	public List<HostCustomers> getAllHostCustomers() {
		return hostCustomersDao.findAll();
	}

	@Override
	public List<HostCustomers> findByCityId(int cityId) {
		return hostCustomersDao.findByCityId(cityId);
	}

	@Override
	public boolean updateHostCustomer(HostCustomers hostCustomer) throws InvalidInputException {
		if (hostCustomer == null) {
			throw new InvalidInputException("Host Customer Cannot Be Null");
		}
		return hostCustomersDao.updateHostCustomer(hostCustomer);
	}

	@Override
	public boolean deleteHostCustomer(int customerId) {
		return hostCustomersDao.deleteByCustomerId(customerId);
	}

	@Override
	public Map<HostCustomers, CustomerInfo> getAllHostCustomersWithInfo()
			throws DatabaseConnectionException, InvalidInputException {
		Map<HostCustomers, CustomerInfo> hostMap = new LinkedHashMap<>();

		List<HostCustomers> hostCustomersList = hostCustomersDao.findAll();

		for (HostCustomers host : hostCustomersList) {
			int customerId = host.getCustomerInfo().getCustomers().getCustomerId();
			CustomerInfo customerInfo = customerInfoService.findByCustomerId(customerId);
			hostMap.put(host, customerInfo);
		}

		return hostMap;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Host vehicle Services
	// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public boolean addHostVehicle(int hostCustomerId, int vehicleId) throws InvalidInputException {
		// Check if vehicle exists
		if (vehiclesDao.getVehicleById(vehicleId) == null) {
			throw new InvalidInputException("Vehicle Not Found");
		}
		return hostVehicleDao.addHostVehicle(hostCustomerId, vehicleId);
	}

	@Override
	public List<Vehicles> findVehiclesByHostCustomerId(int hostCustomerId)
			throws DatabaseConnectionException, InvalidInputException {
		Set<Vehicles> vehicleSet = new HashSet<>(hostVehicleDao.findVehiclesByHostCustomerId(hostCustomerId));
		return new ArrayList<>(vehicleSet); // Returning as List
	}

	@Override
	public boolean deleteHostVehicle(int vehicleId) {
		return hostVehicleDao.deleteHostVehicle(vehicleId);
	}

	@Override
	public int countVehiclesByHostCustomerId(int hostCustomerId) {
		return hostVehicleDao.countVehiclesByHostCustomerId(hostCustomerId);
	}

	@Override
	public BigDecimal getRevenueByHostId(int hostCustomerId) throws InvalidInputException, DatabaseConnectionException {
		if (hostCustomerId <= 0) {
			throw new InvalidInputException("Host ID must be valid and greater than 0.");
		}

		return hostCustomersDao.getRevenueByHostId(hostCustomerId);
	}

}
