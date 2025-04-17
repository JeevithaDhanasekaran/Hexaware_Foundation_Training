package com.hexaware.carrental.service;

import java.util.List;
import java.util.Set;

import com.hexaware.carrental.entity.City;
import com.hexaware.carrental.exception.DatabaseConnectionException;
import com.hexaware.carrental.exception.InvalidInputException;

public interface CityService {

	boolean addCity(City city);

	Set<City> getAllCities();

	City getCityWithHighestLeaseFrequency() throws InvalidInputException, DatabaseConnectionException;

	boolean updateCity(City city) throws DatabaseConnectionException, InvalidInputException;

	boolean deleteCity(int cityId) throws DatabaseConnectionException;

	City getCityByName(String cityName) throws DatabaseConnectionException;

	List<City> getCitiesByState(String state) throws InvalidInputException, DatabaseConnectionException;

}
