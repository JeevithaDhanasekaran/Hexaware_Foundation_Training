package com.hexaware.carrental.entity;

import java.math.BigDecimal;

import com.hexaware.carrental.exception.InvalidInputException;

public class Vehicles {
	//Encapsulation
	private int vehicleId;
	private String make;
	private String model;
	private int manufacturingYear;
	private BigDecimal dailyRate;
	private int passengerCapacity;
	private double engineCapacity;
	private double rating;
	private int totalRating;
	private int ratingCount;
	
	//Constructors
	public Vehicles() {
		
	}
	
	public Vehicles(int vehicleId, String make, String model, int manufacturingYear, BigDecimal dailyRate, int passengerCapacity, 
									double engineCapacity, int rating, int totalRating, int ratingCount) {
		this.vehicleId = vehicleId;
		this.make = make;
		this.model = model;
		this.manufacturingYear = manufacturingYear;
		this.dailyRate = dailyRate;
		this.passengerCapacity = passengerCapacity;
		this.engineCapacity = engineCapacity;
		this.rating = rating;
		this.totalRating = totalRating;
		this.ratingCount = ratingCount;
	}
	
	public Vehicles(String make, String model, int manufacturingYear, BigDecimal dailyRate, int passengerCapacity,
			double engineCapacity) {
		this.make = make;
		this.model = model;
		this.manufacturingYear = manufacturingYear;
		this.dailyRate = dailyRate;
		this.passengerCapacity = passengerCapacity;
		this.engineCapacity = engineCapacity;
	}

	//getters
	public int getVehicleId() {
		return vehicleId;
	}
	public String getMake() {
		return make;
	}
	public String getModel() {
		return model;
	}
	public int getManufacturingYear() {
		return manufacturingYear;
	}
	public BigDecimal getDailyRate() {
		return dailyRate;
	}
	public int getPassengerCapacity() {
		return passengerCapacity;
	}
	public double getEngineCapacity() {
		return engineCapacity;
	}
	public double getRating() {
		return rating;
	}
	public int getTotalRating() {
		return totalRating;
	}
	public int getRatingCount() {
		return ratingCount;
	}
	
	//setters
	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public void setManufacturingYear(int manufacturingYear) {
		this.manufacturingYear = manufacturingYear;
	}
	public void setDailyRate(BigDecimal dailyRate) throws InvalidInputException {
		if(dailyRate.compareTo(BigDecimal.ZERO)< 0) {
			throw new InvalidInputException("Valid Rate per day should be given ");
		}
		this.dailyRate = dailyRate;
	}
	public void setPassengerCapacity(int passengerCapacity) {
		if(passengerCapacity>1) {
			this.passengerCapacity = passengerCapacity;
		}
		else {
			throw new IllegalArgumentException("Passenge Capacity for a car must exceed 1");
		}
		
	}
	public void setEngineCapacity(double engineCapacity) {
		this.engineCapacity = engineCapacity;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	public void setTotalRating(int totalRating) {
		this.totalRating = totalRating;
	}
	public void setRatingCount(int ratingCount) {
		this.ratingCount = ratingCount;
	}
	
	
	@Override
    public String toString() {
        return "Vehicle ID : " + vehicleId +
                ", Make : " + make +
                ", Model : " + model +
                ", Year : " + manufacturingYear +
                ", Rate/Day : " + dailyRate +
                ", Capacity : " + passengerCapacity +
                ", Engine CC : " + engineCapacity +
                ", Rating : " + rating +
                ", Total Rating : " + totalRating +
                ", Rating Count : " + ratingCount;
    }
}
