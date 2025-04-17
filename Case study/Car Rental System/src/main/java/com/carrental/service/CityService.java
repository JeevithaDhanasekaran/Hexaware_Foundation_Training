package com.carrental.service;

import java.util.List;
import java.util.Set;

import com.carrental.entity.City;
import com.carrental.exception.DatabaseConnectionException;
import com.carrental.exception.InvalidInputException;

public interface CityService {

	boolean addCity(City city);

	Set<City> getAllCities();

	City getCityWithHighestLeaseFrequency() throws InvalidInputException, DatabaseConnectionException;

	boolean updateCity(City city) throws DatabaseConnectionException, InvalidInputException;

	boolean deleteCity(int cityId) throws DatabaseConnectionException;

	City getCityByName(String cityName) throws DatabaseConnectionException;

	List<City> getCitiesByState(String state) throws InvalidInputException, DatabaseConnectionException;

}
