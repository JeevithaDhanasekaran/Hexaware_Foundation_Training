package com.carrental.entity;

import com.carrental.exception.InvalidInputException;

public class CustomerInfo {

	//encapsulation
	private Customers customers;//composition
	private int contactId;
	private String email;
	private String phoneNumber;
	private String address;
	private String role;
	
	//constructors
	public CustomerInfo() {}

	public CustomerInfo(Customers customers, int contactId, String email, String phoneNumber, 
			String address, String role) throws InvalidInputException {
		this.setCustomers(customers);
		this.contactId = contactId;
		this.setEmail(email);
		this.setEmail(email);
		this.address = address;
		this.setRole(role);
	}

	//getters
	public Customers getCustomers() {
		return customers;
	}
	public int getContactId() {
		return contactId;
	}
	public String getEmail() {
		return email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public String getRole() {
		return role;
	}
	
	
	//setters
	public void setCustomers(Customers customers) throws InvalidInputException {
		if(customers == null) {
			throw new InvalidInputException("Customer details must be provided.");
		}
		this.customers = customers;
	}
	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	public void setEmail(String email) throws InvalidInputException {
		if (email == null || !email.toLowerCase().matches("^[a-z0-9.]+@[a-z]+\\.[a-z]{2,}$")) {
	        throw new InvalidInputException("Input Validation failed for Email");
	    }
	    this.email = email;
	}

	public void setPhoneNumber(String mobile) throws InvalidInputException {
		if (mobile == null || !mobile.matches("^[6-9]\\d{9}$")) {
	        throw new InvalidInputException("Input Validation Failed for Phone Number");
	    }
	    this.phoneNumber = mobile;
}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setRole(String role) throws InvalidInputException {
	if(role==null ) {
		throw new  InvalidInputException("Input validation failed for Role");
	}
	else
		this.role=role;
}
	
	@Override
	public String toString() {
		return "CustomerInfo{" +
				"customers=" + customers +
				", contactId=" + contactId +
				", email='" + email + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				", address='" + address + '\'' +
				", role='" + role + '\'' +
				'}';
	}
	
}
