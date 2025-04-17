package com.careerhub.entity;

import java.util.Date;
import com.careerhub.entity.Applicant;

public class JobApplication {
	private int applicationID;
    private JobListing jobListing;      // Tight coupling
    private Applicant applicant;        // Tight coupling
    private Date applicationDate;
    private String coverLetter;
    
    public JobApplication() {}

    public JobApplication(int applicationID, JobListing jobListing, Applicant applicant, Date applicationDate, String coverLetter) {
        this.applicationID = applicationID;
        this.setJobListing(jobListing);
        this.setApplicant(applicant);
        this.setApplicationDate(applicationDate);
        this.setCoverLetter(coverLetter);
    }
    
    //Getters and Setters
    
    public int getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(int applicationID) {
        this.applicationID = applicationID;
    }
    
    public JobListing getJobListing() {
        return jobListing;
    }

    public void setJobListing(JobListing jobListing) {
        if (jobListing != null) {
            this.jobListing = jobListing;
        } else {
            throw new IllegalArgumentException("Job listing cannot be null.");
        }
    }
    
    
    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        if (applicant != null) {
            this.applicant = applicant;
        } else {
            throw new IllegalArgumentException("Applicant cannot be null.");
        }
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = (applicationDate != null) ? applicationDate : new Date();
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    @Override
    public String toString() {
        return "JobApplication [applicationID=" + applicationID 
            + ", jobListing=" + jobListing.getJobID()
            + ", applicant=" + applicant.getUserID()
            + ", applicationDate=" + applicationDate 
            + ", coverLetter=" + coverLetter + "]";
    }
    
    
}
