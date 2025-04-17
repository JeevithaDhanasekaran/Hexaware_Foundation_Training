package com.careerhub.entity;

public class Applicant extends User{

	private String resume;

    public Applicant() {}

    public Applicant(int userID, String email, String phone, String firstName, String lastName, String resume) {
        super(userID, email, phone, firstName, lastName,"APPLICANT");
        this.resume = resume;
    }
    
    //getters and setters
    
    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }
    
    public String getName() {
    	return firstName+lastName;
    }

    @Override
    public String toString() {
        return "Applicant [" + super.toString() + ", resume=" + resume + "]";
    }
}
