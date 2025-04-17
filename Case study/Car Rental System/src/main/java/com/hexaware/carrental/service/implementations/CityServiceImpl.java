package com.hexaware.carrental.service.implementations;

import java.sql.Connection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.hexaware.carrental.dao.CityDao;
import com.hexaware.carrental.dao.implementations.CityDaoImpl;
import com.hexaware.carrental.entity.City;
import com.hexaware.carrental.exception.DatabaseConnectionException;
import com.hexaware.carrental.exception.InvalidInputException;
import com.hexaware.carrental.service.CityService;
import com.hexaware.carrental.util.DBconnection;

public class CityServiceImpl implements CityService {

	private Connection connection;
	private CityDao cityDao;

	public CityServiceImpl(String fileName) throws DatabaseConnectionException {
		this.connection = DBconnection.getConnection(fileName);
		this.cityDao = new CityDaoImpl(connection);
	}

	@Override
	public boolean addCity(City city) {
		if (city == null) {
			throw new IllegalArgumentException("City details can't be null");
		}
		return cityDao.addCity(city);
	}

	@Override
	public Set<City> getAllCities() {
		return new LinkedHashSet<>(cityDao.findAllCity());// to ensure insertion order
	}

	@Override
	public City getCityWithHighestLeaseFrequency() throws InvalidInputException, DatabaseConnectionException {
		return cityDao.getCityWithHighestLeaseFrequency();
	}

	@Override
	public boolean updateCity(City city) throws DatabaseConnectionException, InvalidInputException {
		if (city == null) {
			throw new InvalidInputException("City Details Cannot Be Null");
		}
		return cityDao.updateCity(city);
	}

	@Override
	public boolean deleteCity(int cityId) throws DatabaseConnectionException {
		return cityDao.deleteCity(cityId);
	}

	@Override
	public City getCityByName(String cityName) throws DatabaseConnectionException {
		if (cityName == null || cityName.trim().isEmpty()) {
			throw new DatabaseConnectionException("City name cannot be null or empty");
		}
		List<City> cityList = cityDao.findAllCity();

		for (City c : cityList) {
			if (c.getCityName().equalsIgnoreCase(cityName.trim())) {
				return c;
			}
		}
		return null;
	}

	@Override
	public List<City> getCitiesByState(String state) throws InvalidInputException, DatabaseConnectionException {
		if (state == null || state.trim().isEmpty())
			throw new InvalidInputException("State cannot be null or empty");

		return cityDao.getCitiesByState(state);
	}

}
