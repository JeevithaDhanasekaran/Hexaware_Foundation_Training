package com.careerhub.entity;

import java.math.BigDecimal;
import java.util.Date;

public class JobListing {
	private int jobID;
    private Company company; // Tight coupling -composition
    private String jobTitle;
    private String jobDescription;
    private String location;
    private BigDecimal salary;
    private String jobType; 
    private Date postedDate;
    private Date deadline;
    
    public JobListing() {}

    public JobListing(int jobID, Company company, String jobTitle, String jobDescription,
                      String location, BigDecimal salary,String jobType, Date postedDate,Date deadline) {
        this.jobID = jobID;
        this.setCompany(company);
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.location = location;
        this.setSalary(salary);
        this.setJobType(jobType);
        this.postedDate = postedDate;
        this.setDeadline(deadline);
    }
    
 // Getters and Setters

    public int getJobID() {
        return jobID;
    }

    public void setJobID(int jobID) {
        this.jobID = jobID;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
    	if (company != null) {
            this.company = company;
        } else {
            throw new IllegalArgumentException("Company cannot be null");
        }
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
    	if (salary != null && salary.compareTo(BigDecimal.ZERO) >= 0) {
            this.salary = salary;
        } else {
            throw new IllegalArgumentException("Salary must be a non-negative value");
        }
    }
    
    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
    	if (jobType != null && (jobType.toLowerCase().trim().equalsIgnoreCase("Full-time") || 
    			(jobType.toLowerCase().trim().equalsIgnoreCase("Part-time")) || 
    			(jobType.toLowerCase().trim().equalsIgnoreCase("Contract")))) {
            this.jobType = jobType;
        } else {
           throw new IllegalArgumentException("Job Type cannot be null");
        }
    }


    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }
    
    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }
    

    @Override
    public String toString() {
        return "JobListing [jobID=" + jobID + ", company=" + company.getCompanyName() +
               ", jobTitle=" + jobTitle + ", location=" + location + ", salary=" + salary +
               ", Job Type= " + jobType +
               ", postedDate=" + postedDate + 
               ", deadline=" + deadline + "]";
    }
    
    
    
}
