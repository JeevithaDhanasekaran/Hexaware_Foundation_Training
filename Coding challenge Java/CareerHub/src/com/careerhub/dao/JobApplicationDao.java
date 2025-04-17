package com.careerhub.dao;

import java.util.List;

import com.careerhub.entity.JobApplication;
import com.careerhub.exception.ApplicationDeadlineException;

public interface JobApplicationDao {
	boolean addJobApplication(JobApplication jobApplication) throws ApplicationDeadlineException;
    boolean updateJobApplication(JobApplication jobApplication);
    boolean deleteJobApplication(int applicationID);
    JobApplication getJobApplicationByID(int applicationID);
    boolean isApplicationOnTime(JobApplication jobApplication);
    List<JobApplication> getAllJobApplications();
    List<JobApplication> getJobApplicationsByApplicant(int applicantID);
    List<JobApplication> getJobApplicationsByJobListing(int jobID);
}
