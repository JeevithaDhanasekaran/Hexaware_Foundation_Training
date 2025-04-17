package com.hexaware.carrental.entity;

import com.hexaware.carrental.exception.InvalidInputException;

public class Auth {
	
	//encapsulation
	private int authId;
	private Customers customers;//composition
	private String userName;
	private String password;
	
	
	//constructors 
	public Auth() {}
	
	public Auth(int authId, Customers customers, String userName, String password) {
		this.authId = authId;
		this.customers = customers;
		this.userName = userName;
		this.password = password;
	}

	//getters
	public int getAuthId() {
		return authId;
	}

	public Customers getCustomers() {
		return customers;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}
	
	//setters

	public void setAuthId(int authId) {
		this.authId = authId;
	}

	public void setCustomers(Customers customers) {
		this.customers = customers;
	}

	public void setUserName(String userName) throws InvalidInputException {
	if(userName == null || !userName.matches("^[a-zA-Z0-9._+%-@]{3,12}$")) {
		throw new InvalidInputException("Unique Username should be 7-14 characters & allowed: a-z, A-Z, 0-9, . _ + % @ - ");
	}
	else
		this.userName=userName;
}

	public void setPassword(String password) throws InvalidInputException {
		if(password==null || password.trim().isEmpty()  || ! password.matches("^[a-zA-Z0-9._+%-@]{6,12}")) {
			throw new InvalidInputException("Password should be 6-12 characters & allowed: a-z, A-Z, 0-9, Special charcaters:  (. _ + % @ -) ");
		}
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "auth ID: "+authId
				+"CustomerId: "+customers.getCustomerId()
				+"Name: " + customers.getCustomerName()
				+"userName : "+ userName
				+"Password: " + password;
	}
}
