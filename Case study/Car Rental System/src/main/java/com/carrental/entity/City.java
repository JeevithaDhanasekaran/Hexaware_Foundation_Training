package com.carrental.entity;

import com.carrental.exception.InvalidInputException;

public class City {
	
	private int cityId;
	private String cityName;
	private String state;

	// No-arg Constructor
	public City() {
	}

	// Parameterized Constructor
	public City(int cityId, String cityName,String state) throws InvalidInputException {
		this.cityId = cityId;
		setCityName(cityName);
		setState(state);
	}

	// Getters
	public int getCityId() {
		return cityId;
	}

	public String getCityName() {
		return cityName;
	}
	
	public String getState() {
		return state;
	}
	

	// Setters
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	
	public void setState(String state) throws InvalidInputException {
		if (state == null || state.trim().isEmpty() || !state.trim().matches("^[A-Za-z\\s\\-]{3,50}$")) {
	        throw new InvalidInputException("Invalid State Name: Only alphabets & length 3 to 100 allowed");
	    }
	    this.state = state.trim();
	}

	public void setCityName(String cityName) throws InvalidInputException {
		if (cityName == null || cityName.trim().isEmpty() || !cityName.trim().matches("^[A-Za-z\\s\\-]{3,80}$")) {
	        throw new InvalidInputException("Invalid City Name: Only alphabets & length 3 to 50 allowed");
	    }
	    this.cityName = cityName.trim();
	}

	@Override
	public String toString() {
		return String.format("City ID: " + cityId + ", City Name: " + cityName+"  State:  "+state);
	}
}
