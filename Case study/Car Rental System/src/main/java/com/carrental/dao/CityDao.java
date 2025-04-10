package com.carrental.dao;

import java.util.List;

import com.carrental.entity.City;

public interface CityDao {
	boolean addCity(City city);

	City findCityById(int cityId);

	City findCityByName(String cityName);

	List<City> findAllCity();

	boolean updateCity(City city);

	boolean deleteCity(int cityId);
	
	City findCityIdByName(String cityName);
}
