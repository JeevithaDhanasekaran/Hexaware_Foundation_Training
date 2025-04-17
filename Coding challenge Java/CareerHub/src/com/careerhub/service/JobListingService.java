package com.careerhub.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.careerhub.entity.JobListing;

public interface JobListingService {
	boolean postJob(JobListing jobListing);
    List<JobListing> getAllJobListings();
    Set<JobListing> getJobListingsBySalaryRange(BigDecimal minSalary, BigDecimal maxSalary);
    List<JobListing> getJobListingsByCompany(int companyID);
    JobListing getJobListingByID(int jobID);
    boolean updateJobListing(JobListing jobListing);
    boolean deleteJobListing(int jobID);
}
