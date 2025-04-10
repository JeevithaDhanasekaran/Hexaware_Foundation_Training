package com.carrental.entity;

import com.carrental.exception.InvalidInputException;

public class Customers {
	
	//encapsulation
	private int customerId;
    private String firstName;
    private String lastName;
    private String licenseNumber;

	//default constructor
	public Customers() {
		
	}
	
	//parameterized constructor
	public Customers(int customerId, String firstName, String lastName,String licenseNumber) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName=lastName;
        this.licenseNumber=licenseNumber;

    }
	
	//getters
	public int getCustomerId() {
		return customerId;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getLicenseNumber() {
		return licenseNumber;
	}

	public String getCustomerName() {
		return new String(firstName+" "+lastName);
	}
	
	
//setters
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setLicenseNumber(String licenseNumber) throws InvalidInputException {
		if(licenseNumber == null || licenseNumber.trim().isEmpty() || ! licenseNumber.trim().matches("^[A-Za-z]{2}\\d{7}$")){
			throw new InvalidInputException("Invalid license number format.");
		}
		else
			this.licenseNumber=licenseNumber;
	}
	
	@Override
	public String toString() {
		return "Customer { " +
		           "ID=" + customerId +
		           ", First Name='" + firstName + '\'' +
		           ", Last Name='" + lastName + '\'' +
		           " }";
	}
	
}
