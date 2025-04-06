package com.techshop.entities;

import java.util.List;

/*
 * Customers Class:
Attributes:
• CustomerID (int)
• FirstName (string)
• LastName (string)
• Email (string)
• Phone (string)
• Address (string)*/

public class Customers {
	
	//attributes -> encapsulation
	private int customerID;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private String address;
	
	//default constructor
	public Customers() {
		
	}
	//parameterized constructor
	public Customers(int customerID, String firstName, String lastName, String email, String phoneNumber,
			String address) {
		
		this.customerID = customerID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address = address;
	}
	
	//getters and setters
	//no setter for customer id, firstname,lastname
	public int getCustomerID() {
		return customerID;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
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
	
	public void setCustomerID(int customerID) {
		this.customerID= customerID;
	}
	public void setFirstName(String firstName) {
		this.firstName=firstName;
	}
	public void setLastName(String lastName) {
		this.lastName= lastName;
	}
	
	//validating email
	public void setEmail(String email) {
		if (email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        this.email = email;
        }
	
	//phoneNumber validation checking for exact 10 digits
	public void setPhoneNumber(String phoneNumber) {
		if (phoneNumber == null || !phoneNumber.matches("\\d{10}")) {
            throw new IllegalArgumentException("Invalid phone number. Must be 10 digits.");
        }
        this.phoneNumber = phoneNumber;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	//UpdateCustomerInfo(): Allows the customer to update their information (e.g., email, phone, or address).
	public void updateCustomerInfo(String email, String phone, String address) {
		setEmail(email); //  to ensure validation is executed
	    setPhoneNumber(phone); // to ensure validation
	    this.address = address; 
    }
	
    //GetCustomerDetails(): Retrieves and displays detailed information about the customer.
    public void getCustomerDetails() {
    	System.out.println(String.format("Customer ID: %d, Name: %s %s, Email: %s, Phone: %s, Address: %s",
                customerID, firstName, lastName, email, phoneNumber, address));
    }

//    CalculateTotalOrders(): Calculates the total number of orders placed by a customer.
    public int calculateTotalOrders(List<Orders> orders) {
    	if (orders == null) {
            return 0; // Or throw an exception post development if needed
        }
    	//streams to count the total orders
        return (int) orders.stream().filter(o -> o.getCustomer().getCustomerID() == this.customerID).count();
    }
    
    public String toString() {
        return "Customer [ID=" + customerID + 
               ", FirstName=" + firstName +
               ", LastName= "+lastName +
               ", Email=" + email + 
               ", Phone=" + phoneNumber + "]";
    }
	
}
