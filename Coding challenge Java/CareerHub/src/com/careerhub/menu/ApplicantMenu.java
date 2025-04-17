package com.careerhub.menu;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.careerhub.entity.Applicant;
import com.careerhub.entity.JobApplication;
import com.careerhub.entity.JobListing;
import com.careerhub.exception.ApplicationDeadlineException;
import com.careerhub.service.ApplicantService;
import com.careerhub.service.JobApplicationService;
import com.careerhub.service.JobListingService;
import com.careerhub.service.Impl.ApplicantServiceImpl;
import com.careerhub.service.Impl.JobApplicationServiceImpl;
import com.careerhub.service.Impl.JobListingServiceImpl;

public class ApplicantMenu {
	private Scanner scanner;
	private ApplicantService applicantService;
	private JobListingService jobListingService;
	private JobApplicationService jobApplicationService;
	private Applicant loggedInApplicant;

	public ApplicantMenu(String filename,Applicant loggedInApplicant) {
		this.scanner = new Scanner(System.in);
		this.applicantService = new ApplicantServiceImpl(filename);
		this.jobListingService = new JobListingServiceImpl(filename);
		this.jobApplicationService = new JobApplicationServiceImpl(filename);
		this.loggedInApplicant = loggedInApplicant;
	}
	
	public void displayMenu() throws ApplicationDeadlineException {
        int choice;
        do {
            System.out.println("\n--- Applicant Menu ---");
            System.out.println("1. View All Job Listings");
            System.out.println("2. Apply for a Job");
            System.out.println("3. View My Applications");
            System.out.println("4. View Job Listings by Salary Range");
            System.out.println("0. Logout");
            System.out.print("Enter choice: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    viewAllJobListings();
                    break;
                case 2:
                    applyForJob();
                    break;
                case 3:
                    viewMyApplications();
                    break;
                case 4:
                    viewJobListingsBySalaryRange();
                    break;
                case 0:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } while (choice != 0);
    }

	private void viewJobListingsBySalaryRange() {
		try {
	        System.out.print("Enter minimum salary: ");
	        BigDecimal min = new BigDecimal(scanner.nextLine());

	        System.out.print("Enter maximum salary: ");
	        BigDecimal max = new BigDecimal(scanner.nextLine());

	        Set<JobListing> listings = jobListingService.getJobListingsBySalaryRange(min, max);
	        if (listings.isEmpty()) {
	            System.out.println("No job listings found in this salary range.");
	        } else {
	            System.out.println("--- Job Listings in Salary Range ---");
	            listings.forEach(System.out::println);
	        }
	    } catch (NumberFormatException e) {
	        System.out.println("Invalid salary input. Please enter valid numbers.");
	    }

	}

	private void viewMyApplications() {
		List<JobApplication> applications = jobApplicationService.getApplicationsByApplicant(loggedInApplicant.getUserID());
        if (applications.isEmpty()) {
            System.out.println("You have not applied to any jobs yet.");
        } else {
            System.out.println("\n--- My Job Applications ---");
            for (JobApplication app : applications) {
                System.out.println("Application ID: " + app.getApplicationID());
                System.out.println("Job Title: " + app.getJobListing().getJobTitle());
                System.out.println("Company: " + app.getJobListing().getCompany().getCompanyName());
                System.out.println("Applied on: " + app.getApplicationDate());
                System.out.println("Cover Letter: " + app.getCoverLetter());
                System.out.println("---------------------------");
            }
        }
		
	}

	private void applyForJob() throws ApplicationDeadlineException {
		System.out.println("--- Available Job Listings ---");
	    List<JobListing> listings = jobListingService.getAllJobListings();
	    if (listings.isEmpty()) {
	        System.out.println("No job listings available.");
	        return;
	    }

	    listings.forEach(job -> System.out.println(job));
	    
	    System.out.print("Enter Job ID to apply: ");
	    int jobID = Integer.parseInt(scanner.nextLine());

	    JobListing jobListing = jobListingService.getJobListingByID(jobID);
	    if (jobListing == null) {
	        System.out.println("Invalid Job ID.");
	        return;
	    }

	    System.out.print("Enter your cover letter: ");
	    String coverLetter = scanner.nextLine();

	    JobApplication application = new JobApplication();
	    application.setJobListing(jobListing);
	    application.setApplicant(loggedInApplicant);
	    application.setApplicationDate(new Date());
	    application.setCoverLetter(coverLetter);

	    boolean success = jobApplicationService.applyForJob(application);
	    System.out.println(success ? "Application submitted!" : "Failed to apply.");
		
	}

	private void viewAllJobListings() {
		List<JobListing> jobs = jobListingService.getAllJobListings();
        if (jobs.isEmpty()) {
            System.out.println("No job listings found.");
        } else {
            System.out.println("\n--- Available Job Listings ---");
            for (JobListing job : jobs) {
                System.out.println("Job ID: " + job.getJobID());
                System.out.println("Title: " + job.getJobTitle());
                System.out.println("Company: " + job.getCompany().getCompanyName());
                System.out.println("Salary: " + job.getSalary());
                System.out.println("Location: " + job.getLocation());
                System.out.println("---------------------------");
            }
        }
		
	}
	

}
