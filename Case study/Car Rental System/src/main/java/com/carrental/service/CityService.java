package com.carrental.service;

import java.util.List;

import com.carrental.entity.City;

public interface CityService {
	boolean addCity(City city);
    List<City> getAllCities();
    City getCityWithHighestLeaseFrequency();
}
