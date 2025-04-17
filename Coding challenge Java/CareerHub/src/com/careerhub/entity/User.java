package com.careerhub.entity;

import com.careerhub.exception.InvalidEmailFormatException;
import com.careerhub.exception.InvalidPhoneNumberFormatException;

public abstract class User {
	
	//inherited classes can have access to these instance variables
	protected int userID;
    protected String email;
    protected String phone;
    protected String firstName;
    protected String lastName;
    protected String userType;
    
    
    public User() {}

    public User(int userID, String email, String phone, String firstName, String lastName,String userType) {
        try {
    	this.userID = userID;
        this.setEmail(email);
        this.setPhone(phone);
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType=userType;
        }catch(InvalidEmailFormatException | InvalidPhoneNumberFormatException e) {
        	e.printStackTrace();
        	System.out.println("Error setting values in  User entity ");
        }
    }

 // Getters and Setters

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws InvalidEmailFormatException {
    	if (email == null || !email.toLowerCase().matches("^[a-z0-9.]+@[a-z]+\\.[a-z]{2,}$")) {
	        throw new InvalidEmailFormatException("Input Validation failed for Email");
	    }
	    this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) throws InvalidPhoneNumberFormatException {
    	if (phone == null || !phone.matches("^[6-9]\\d{9}$")) {
	        throw new InvalidPhoneNumberFormatException("Input Validation Failed for Phone Number");
	    }
	    this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getUserType() {
    	return userType;
    }
    public void setUserType(String userType) {
    	if (userType != null && (userType.toLowerCase().trim().equalsIgnoreCase("APPLICANT") || 
    			(userType.toLowerCase().trim().equalsIgnoreCase("COMPANY")) )) {
            this.userType = userType;
        } else {
           throw new IllegalArgumentException("Job Type cannot be null");
        }
    }

    @Override
    public String toString() {
        return "User [userID=" + userID + ", email=" + email + ", phone=" + phone +
               ", firstName=" + firstName + ", lastName=" + lastName + "]";
    }
    
}
