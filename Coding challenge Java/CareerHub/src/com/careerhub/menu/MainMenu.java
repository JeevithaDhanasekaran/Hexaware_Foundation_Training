package com.careerhub.menu;

import java.util.List;
import java.util.Scanner;

import com.careerhub.entity.Applicant;
import com.careerhub.entity.Company;
import com.careerhub.entity.JobListing;
import com.careerhub.exception.ApplicationDeadlineException;
import com.careerhub.exception.InvalidEmailFormatException;
import com.careerhub.exception.InvalidPhoneNumberFormatException;
import com.careerhub.service.ApplicantService;
import com.careerhub.service.CompanyService;
import com.careerhub.service.JobApplicationService;
import com.careerhub.service.JobListingService;
import com.careerhub.service.Impl.ApplicantServiceImpl;
import com.careerhub.service.Impl.CompanyServiceImpl;
import com.careerhub.service.Impl.JobApplicationServiceImpl;
import com.careerhub.service.Impl.JobListingServiceImpl;

public class MainMenu {
    private final Scanner scanner;
    private final ApplicantService applicantService;
    private final CompanyService companyService;
    private final JobListingService jobListingService;
    private JobApplicationService jobApplicationService;
    private String filename;
    

    public MainMenu(String filename) {
        this.applicantService = new ApplicantServiceImpl(filename);
        this.companyService = new CompanyServiceImpl(filename);
        this.jobListingService = new JobListingServiceImpl(filename);
        this.scanner = new Scanner(System.in);
        this.jobApplicationService = new JobApplicationServiceImpl(filename);
        this.filename=filename;
    }

    public void display() throws InvalidEmailFormatException, InvalidPhoneNumberFormatException, ApplicationDeadlineException {
        int choice;
        do {
            System.out.println("\n==== CareerHub Main Menu ====");
            System.out.println("1. Register as Company");
            System.out.println("2. Register as Applicant");
            System.out.println("3. Login as Company");
            System.out.println("4. Login as Applicant");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> registerAsCompany();
                case 2 -> registerAsApplicant();
                case 3 -> loginAsCompany();
                case 4 -> loginAsApplicant();
                case 0 -> System.out.println("Exiting CareerHub. Goodbye!");
                default -> System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 0);
    }

    private void registerAsApplicant() throws InvalidEmailFormatException, InvalidPhoneNumberFormatException {
    	System.out.println("\nRegister as Applicant");

        System.out.print("Enter first name: ");
        String fname = scanner.nextLine();
        
        System.out.print("Enter last name: ");
        String lname = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();

        System.out.print("Enter resume text: ");
        String resume = scanner.nextLine();

        Applicant applicant = new Applicant(0, email, phone, fname, lname, resume);

        boolean success = applicantService.registerApplicant(applicant);
        if (success) {
            System.out.println("Applicant registered successfully!");
        } else {
            System.out.println("Failed to register applicant.");
        }
    }

    private void registerAsCompany() {
    	System.out.println("\n--- Register as Company ---");

        System.out.print("Enter company name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter Location: ");
        String description = scanner.nextLine().trim();
        
        System.out.print("Enter your phone Number: ");
        String phone = scanner.nextLine().trim();
        
        System.out.print("Enter your firstName : ");
        String fname = scanner.nextLine().trim();
        
        System.out.print("Enter your lastName: ");
        String lname = scanner.nextLine().trim();

        Company company = new Company();
        
        try {
        	company.setCompanyName(name);
			company.setEmail(email);
			company.setLocation(description);
			company.setFirstName(fname);
	        company.setLastName(lname);
	        company.setPhone(phone);
		} catch (InvalidEmailFormatException | InvalidPhoneNumberFormatException e) {
			e.printStackTrace();
		}
        

        boolean success = companyService.registerCompany(company);
        if (success) {
            System.out.println("Company registered successfully!");
        } else {
            System.out.println("Failed to register company.");
        }
    }

    private void loginAsApplicant() throws ApplicationDeadlineException {
    	System.out.println("\n--- Applicant Login ---");

        System.out.print("Enter your email: ");
        String email = scanner.nextLine().trim();

        Applicant applicant = applicantService.getApplicantByEmail(email);
        if (applicant != null) {
            System.out.println("Welcome, " + applicant.getName() + "!");
            new ApplicantMenu(filename, applicant).displayMenu();
        } else {
            System.out.println("Applicant not found.");
        }
    }

    private void loginAsCompany() {
    	System.out.println("\nCompany Login");

        System.out.print("Enter your company email: ");
        String email = scanner.nextLine().trim();
        
        

        Company company=companyService.getCompanyByEmail(email);
        
        if (company != null) {
            System.out.println("Welcome, " + company.getCompanyName() + "!");
            new CompanyMenu(filename, company).displayMenu();
        } else {
            System.out.println("Company not found.");
        }
    }
}
