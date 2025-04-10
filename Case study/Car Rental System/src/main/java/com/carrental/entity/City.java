package com.carrental.entity;

import com.carrental.exception.InvalidInputException;

public class City {
	
	private int cityId;
	private String cityName;

	// No-arg Constructor
	public City() {
	}

	// Parameterized Constructor
	public City(int cityId, String cityName) {
		this.cityId = cityId;
		this.cityName = cityName;
	}

	// Getters
	public int getCityId() {
		return cityId;
	}

	public String getCityName() {
		return cityName;
	}

	// Setters
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public void setCityName(String cityName) throws InvalidInputException {
		if (cityName == null || cityName.trim().isEmpty() || !cityName.matches("^[A-Za-z ]{3,50}$")) {
			throw new InvalidInputException("Invalid City Name: Only alphabets & length 3 to 50 allowed");
		}
		this.cityName = cityName;
	}

	@Override
	public String toString() {
		return String.format("City ID: " + cityId + ", City Name: " + cityName);
	}
}
