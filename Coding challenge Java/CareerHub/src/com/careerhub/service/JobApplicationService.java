package com.careerhub.service;

import java.util.List;
import java.util.Set;

import com.careerhub.entity.JobApplication;
import com.careerhub.exception.ApplicationDeadlineException;

public interface JobApplicationService {
	boolean applyForJob(JobApplication jobApplication) throws ApplicationDeadlineException;
    boolean isApplicationOnTime(JobApplication jobApplication);
    Set<JobApplication> getAllJobApplications();
    List<JobApplication> getApplicationsByApplicant(int applicantID);
    List<JobApplication> getApplicationsForJob(int jobID);
}
