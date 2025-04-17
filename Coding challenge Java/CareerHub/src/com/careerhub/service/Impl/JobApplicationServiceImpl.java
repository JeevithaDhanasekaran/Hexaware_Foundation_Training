package com.careerhub.service.Impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.careerhub.dao.JobApplicationDao;
import com.careerhub.dao.impl.JobApplicationDaoImpl;
import com.careerhub.entity.JobApplication;
import com.careerhub.exception.ApplicationDeadlineException;
import com.careerhub.service.JobApplicationService;

public class JobApplicationServiceImpl implements JobApplicationService{

	private JobApplicationDao jobApplicationDao;

    public JobApplicationServiceImpl(String fileName) {
        this.jobApplicationDao = new JobApplicationDaoImpl(fileName);
    }

    public boolean applyForJob(JobApplication jobApplication) throws ApplicationDeadlineException {
        return jobApplicationDao.addJobApplication(jobApplication);
    }

    public boolean isApplicationOnTime(JobApplication jobApplication) {
        return jobApplicationDao.isApplicationOnTime(jobApplication);
    }

    public Set<JobApplication> getAllJobApplications() {
        return new HashSet<>(jobApplicationDao.getAllJobApplications());
    }

    public List<JobApplication> getApplicationsByApplicant(int applicantID) {
        return jobApplicationDao.getJobApplicationsByApplicant(applicantID);
    }

    public List<JobApplication> getApplicationsForJob(int jobID) {
        return jobApplicationDao.getJobApplicationsByJobListing(jobID);
    }
	
}
