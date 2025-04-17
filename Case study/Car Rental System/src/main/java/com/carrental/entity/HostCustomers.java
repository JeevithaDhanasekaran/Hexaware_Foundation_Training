package com.carrental.entity;

import com.carrental.exception.InvalidInputException;

public class HostCustomers {
	//encapsulation
	private int hostCustomerId;
	private CustomerInfo customerInfo;// composition
	private City city;
	private String gstNumber;
	
	
	//constructors
	public HostCustomers() {}
	
	public HostCustomers(int hostCustomerId, CustomerInfo customerInfo, City city, String gstNumber) {
		this.hostCustomerId = hostCustomerId;
		this.customerInfo = customerInfo;
		this.city = city;
		this.gstNumber = gstNumber;
	}

	
	//setters
	public void setHostCustomerId(int hostCustomerId) {
		this.hostCustomerId = hostCustomerId;
	}

	public void setCustomerInfo(CustomerInfo customerInfo) throws InvalidInputException {
		if(customerInfo==null) {
			throw new InvalidInputException("Customer Details must be given for a hostcustomer");
		}
		this.customerInfo = customerInfo;
	}

	public void setCity(City city) throws InvalidInputException {
		if(city==null) {
			throw new InvalidInputException("City must be given for a hostcustomer");
		}
		this.city = city;
	}

	/*
A typical GST Identification Number (GSTIN) in India has a format like this:

First two digits: Represent the state code.
Next ten digits: Represent the PAN (Permanent Account Number) of the business or individual.
Thirteenth digit: Represents the entity code (usually a digit or letter, representing the number of registrations within the same state and PAN).
Fourteenth digit: Is always 'Z' by default.
Fifteenth digit: Is a checksum digit (can be a number or a letter).
Therefore, the total length is 15 characters
	 * */
	public void setGstNumber(String gstNumber) throws InvalidInputException {
		if(gstNumber == null || gstNumber.trim().isEmpty() || ! gstNumber.trim().matches("^[0-9]{2}[A-Za-z]{5}[0-9]{4}[A-Za-z]{1}[0-9A-Za-z]{1}[Zz]{1}[0-9A-Za-z]{1}$")){
			throw new InvalidInputException("Invalid GST number format.");
		}
		else
			this.gstNumber=gstNumber;
	}

	
	//getters
	public int getHostCustomerId() {
		return hostCustomerId;
	}

	public CustomerInfo getCustomerInfo() {
		return customerInfo;
	}

	public City getCity() {
		return city;
	}

	public String getGstNumber() {
		return gstNumber;
	}
	
	@Override
	public String toString() {
	    return "Host Customer ID: " + hostCustomerId + "\n"
	         + "City: " + city.getCityName() + "\n"
	         + "Customer Info: " + customerInfo + "\n"
	         + "Host Customer Name: " + customerInfo.getCustomers().getCustomerName() + "\n"
	         + "GST Number: " + gstNumber;
	}

	
	
}
