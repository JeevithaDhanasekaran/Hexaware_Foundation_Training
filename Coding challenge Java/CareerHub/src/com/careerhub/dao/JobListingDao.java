package com.careerhub.dao;

import java.util.List;

import com.careerhub.entity.JobListing;

public interface JobListingDao {
	boolean addJobListing(JobListing jobListing);
    boolean updateJobListing(JobListing jobListing);
    boolean deleteJobListing(int jobID);
    JobListing getJobListingByID(int jobID);
    List<JobListing> getAllJobListings();
    List<JobListing> getJobListingsByCompany(int companyID);
}
