package com.careerhub.menu;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.careerhub.entity.Company;
import com.careerhub.entity.JobApplication;
import com.careerhub.entity.JobListing;
import com.careerhub.service.CompanyService;
import com.careerhub.service.JobApplicationService;
import com.careerhub.service.JobListingService;
import com.careerhub.service.Impl.CompanyServiceImpl;
import com.careerhub.service.Impl.JobApplicationServiceImpl;
import com.careerhub.service.Impl.JobListingServiceImpl;

public class CompanyMenu {

	private Scanner scanner;
	private CompanyService companyService;
	private JobListingService jobListingService;
	private JobApplicationService jobApplicationService;
	private Company loggedInCompany;

	public CompanyMenu(String filename, Company loggedInCompany) {
		this.scanner = new Scanner(System.in);
		this.companyService = new CompanyServiceImpl(filename);
		this.jobListingService = new JobListingServiceImpl(filename);
		this.jobApplicationService = new JobApplicationServiceImpl(filename);
		this.loggedInCompany = loggedInCompany;
	}
	
	public void displayMenu() {
        int choice;
        do {
            System.out.println("\n--- Company Menu ---");
            System.out.println("1. Post a Job");
            System.out.println("2. View My Job Listings");
            System.out.println("3. View Applications for a Job");
            System.out.println("0. Logout");
            System.out.print("Enter choice: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    postJob();
                    break;
                case 2:
                    viewMyJobs();
                    break;
                case 3:
                    viewApplicationsForJob();
                    break;
                case 0:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } while (choice != 0);
    }

	private void postJob() {
		System.out.print("Enter Job Title: ");
        String title = scanner.nextLine();

        System.out.print("Enter Job Description: ");
        String description = scanner.nextLine();

        System.out.print("Enter Salary: ");
        BigDecimal salary = scanner.nextBigDecimal();
        scanner.nextLine();
        
        System.out.print("Enter Job Location: ");
        String location = scanner.nextLine();
        
        System.out.print("Enter Job Type (e.g., Full-time, Part-time): ");
        String jobType = scanner.nextLine();
        
        System.out.print("Enter Job Deadline (YYYY-MM-DD HH:MM:SS): ");
        String deadlineString = scanner.nextLine();
        Timestamp deadline;
        
        try {
            deadline = Timestamp.valueOf(deadlineString);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD HH:MM:SS format. Job posting failed.");
            return;
        }

        JobListing job = new JobListing();
        
        job.setCompany(loggedInCompany);
        job.setJobTitle(title);
        job.setLocation(location);
        job.setJobDescription(description);
        job.setJobType(jobType);
        job.setSalary(salary);
        job.setPostedDate(new java.util.Date());
        job.setDeadline(deadline);

        boolean success = jobListingService.postJob(job);
        System.out.println(success ? "Job posted successfully!" : "Failed to post job.");
		
	}

	private void viewMyJobs() {
		List<JobListing> jobs = jobListingService.getJobListingsByCompany(loggedInCompany.getUserID());
        if (jobs.isEmpty()) {
            System.out.println("You haven't posted any jobs yet.");
        } else {
            System.out.println("\n--- Your Job Listings ---");
            for (JobListing job : jobs) {
                System.out.println("Job ID: " + job.getJobID());
                System.out.println("Title: " + job.getJobTitle());
                System.out.println("Location:  "+ job.getJobTitle());
                System.out.println("Job Type:  "+ job.getJobType());
                System.out.println("Salary: " + job.getSalary());
                System.out.println("Description: " + job.getLocation());
                System.out.println("Posted Date  : "+job.getPostedDate());
                System.out.println("Deadline  :  "+job.getDeadline());
                System.out.println("------------------------");
            }
        }
	}

	private void viewApplicationsForJob() {
		System.out.print("Enter the Job ID to view applications: ");
        int jobId = Integer.parseInt(scanner.nextLine());

        List<JobApplication> applications = jobApplicationService.getApplicationsForJob(jobId);
        if (applications.isEmpty()) {
            System.out.println("No applications for this job.");
        } else {
            System.out.println("\n--- Applications for Job ID " + jobId + " ---");
            for (JobApplication app : applications) {
                System.out.println("Application ID: " + app.getApplicationID());
                System.out.println("Applicant Name: " + app.getApplicant().getName());
                System.out.println("Cover Letter: " + app.getCoverLetter());
                System.out.println("Applied On: " + app.getApplicationDate());
                System.out.println("------------------------");
            }
        }
		
	}

}
