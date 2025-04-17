package com.careerhub.entity;

import com.careerhub.exception.InvalidPhoneNumberFormatException;

public class Company extends User{
	private String companyName;
    private String location;

    public Company() {}

    public Company(int userID, String email, String phone, String firstName, String lastName, String companyName, String location) {
        super(userID, email, phone, firstName, lastName,"COMPANY");
        this.setCompanyName(companyName);
        this.setLocation(location);
    }
    public void setPhoneNumber(String phoneNumber) throws InvalidPhoneNumberFormatException {
    	super.setPhone(phoneNumber);
    }
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
    	if (companyName != null && !companyName.trim().isEmpty()) {
            this.companyName = companyName;
        } else {
            throw new IllegalArgumentException("Company name cannot be null or empty");
        }
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
    	if (location != null && !location.trim().isEmpty()) {
            this.location = location;
        } else {
            throw new IllegalArgumentException("Location cannot be null or empty");
        }
    }

    @Override
    public String toString() {
        return "Company [" + super.toString() + ", companyName=" + companyName + ", location=" + location + "]";
    }
    
}
