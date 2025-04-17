package com.careerhub.service.Impl;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.careerhub.dao.JobListingDao;
import com.careerhub.dao.impl.JobListingDaoImpl;
import com.careerhub.entity.JobListing;
import com.careerhub.service.JobListingService;

public class JobListingServiceImpl implements JobListingService{
	
	private JobListingDao jobListingDao;

    public JobListingServiceImpl(String fileName) {
        this.jobListingDao = new JobListingDaoImpl(fileName);
    }

	@Override
	public boolean postJob(JobListing jobListing) {
		return jobListingDao.addJobListing(jobListing);
	}

	@Override
	public List<JobListing> getAllJobListings() {
		return jobListingDao.getAllJobListings();
	}

	@Override
	public Set<JobListing> getJobListingsBySalaryRange(BigDecimal minSalary, BigDecimal maxSalary) {
		return new HashSet<>(jobListingDao.getAllJobListings().stream()
	            .filter(job -> job.getSalary() != null && job.getSalary().compareTo(minSalary) >= 0 && job.getSalary().compareTo(maxSalary) <= 0)
	            .collect(Collectors.toList()));
	}

	@Override
	public List<JobListing> getJobListingsByCompany(int companyID) {
		return jobListingDao.getJobListingsByCompany(companyID);
	}

	@Override
	public JobListing getJobListingByID(int jobID) {
		return jobListingDao.getJobListingByID(jobID);
	}

	@Override
	public boolean updateJobListing(JobListing jobListing) {
		 return jobListingDao.updateJobListing(jobListing);
	}

	@Override
	public boolean deleteJobListing(int jobID) {
		return jobListingDao.deleteJobListing(jobID);
	}

}
