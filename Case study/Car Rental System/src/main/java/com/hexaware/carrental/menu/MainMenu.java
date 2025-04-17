package com.hexaware.carrental.menu;

import java.util.Scanner;

import com.hexaware.carrental.entity.Auth;
import com.hexaware.carrental.entity.City;
import com.hexaware.carrental.entity.CustomerInfo;
import com.hexaware.carrental.entity.Customers;
import com.hexaware.carrental.entity.HostCustomers;
import com.hexaware.carrental.exception.DatabaseConnectionException;
import com.hexaware.carrental.exception.InvalidInputException;
import com.hexaware.carrental.exception.PaymentRequiredException;
import com.hexaware.carrental.service.AuthService;
import com.hexaware.carrental.service.CityService;
import com.hexaware.carrental.service.CustomerInfoService;
import com.hexaware.carrental.service.CustomersService;
import com.hexaware.carrental.service.HostCustomersService;
import com.hexaware.carrental.service.implementations.AuthServiceImpl;
import com.hexaware.carrental.service.implementations.CityServiceImpl;
import com.hexaware.carrental.service.implementations.CustomerInfoServiceImpl;
import com.hexaware.carrental.service.implementations.CustomersServiceImpl;
import com.hexaware.carrental.service.implementations.HostCustomersServiceImpl;

public class MainMenu {
	private Scanner scan;

	private String filename;
	private AuthService authService;
	private CustomersService customersService;
	private CustomerInfoService customerInfoService;
	private CityService cityService;
	private HostCustomersService hostService;

	public MainMenu(String filename) throws DatabaseConnectionException {
		this.filename = filename;
		this.scan = new Scanner(System.in);
		this.authService = new AuthServiceImpl(filename);
		this.customerInfoService = new CustomerInfoServiceImpl(filename);
		this.customersService = new CustomersServiceImpl(filename);
		this.cityService = new CityServiceImpl(filename);
		this.hostService = new HostCustomersServiceImpl(filename);

	}

	public void displayMainMenu() throws DatabaseConnectionException, InvalidInputException, PaymentRequiredException {
		int choice;
		do {
			System.out.println("\n========== Welcome to Car Rental System ==========");
			System.out.println("1. Admin Login");
			System.out.println("2. Customer Login");
			System.out.println("3. Host Customer Login");
			System.out.println("4. Register as Customer");
			System.out.println("5. Register as Host Customer");
			System.out.println("6. Exit");
			System.out.print("Enter your choice: ");
			choice = scan.nextInt();
			scan.nextLine();

			switch (choice) {
			case 1:
				handleLoginByRole("ADMIN");
				break;
			case 2:
				handleLoginByRole("CUSTOMER");
				break;
			case 3:
				handleLoginByRole("HOST");
				break;
			case 4:
				registerAsCustomer();
				break;
			case 5:
				registrationAsHostCustomer();
				break;
			case 6:
				System.out.println("Thank you for using Car Rental System. Exiting...");
				break;
			default:
				System.out.println("Invalid Choice! Please try again.");
			}
		} while (choice != 6);

	}

	private void registrationAsHostCustomer() {
		try {
			System.out.println("\n--- Host Customer Registration ---");

			// Step 1: Use customer registration process
			registerAsCustomer();

			// Step 2: Get registered customer (by asking ID or last record)
			System.out.print("Enter your new Customer ID to register as Host: ");
			int customerId = scan.nextInt();
			scan.nextLine();

			Customers customer = customersService.findCustomerById(customerId);
			CustomerInfo customerInfo = customerInfoService.findByCustomerId(customerId);

			// Step 3: Get hosting city and GST
			System.out.print("Enter Hosting City Name: ");
			String cityName = scan.nextLine();

			City city = cityService.getCityByName(cityName);

			System.out.print("Enter GST Number: ");
			String gst = scan.nextLine();

			// Step 4: Add Host Customer
			HostCustomers host = new HostCustomers();
			host.setCustomerInfo(customerInfo);
			host.setCity(city);
			host.setGstNumber(gst);

			if (hostService.addHostCustomer(host)) {
				System.out.println("Host customer registered successfully!");
			} else {
				System.out.println("Failed to register as host customer.");
			}

		} catch (Exception e) {
			System.out.println("Error during host registration: " + e.getMessage());
		}

	}

	private void registerAsCustomer() {
		try {
			System.out.println("\n--- New Customer Registration ---");

			// Step 1: Get customer details
			System.out.print("Enter First Name: ");
			String firstName = scan.nextLine();
			System.out.print("Enter Last Name: ");
			String lastName = scan.nextLine();
			System.out.print("Enter License Number (Format: AA1234567): ");
			String licenseNumber = scan.nextLine();

			Customers customer = new Customers(firstName, lastName, licenseNumber);

			boolean isCustomerAdded = customersService.addCustomer(customer);
			if (!isCustomerAdded) {
				System.out.println("Failed to add customer.");
				return;
			}

			int customerId = customersService.getCustomerIdByLicense(licenseNumber);
			if (customerId <= 0) {
				System.out.println("Failed to retrieve customer ID after insertion.");
				return;
			}
			customer.setCustomerId(customerId);

			// Step 2: Add Customer Info
			System.out.print("Enter Email: ");
			String email = scan.nextLine();
			System.out.print("Enter Phone Number: ");
			String phone = scan.nextLine();
			System.out.print("Enter Address: ");
			String address = scan.nextLine();

			CustomerInfo customerInfo = new CustomerInfo();
			customerInfo.setCustomers(customer);
			customerInfo.setEmail(email);
			customerInfo.setPhoneNumber(phone);
			customerInfo.setAddress(address);
			customerInfo.setRole("Customer");

			boolean isInfoAdded = customerInfoService.addCustomerInfo(customerInfo);
			if (!isInfoAdded) {
				System.out.println("Failed to add customer contact info.");
				return;
			}

			// Step 3: Register Auth (validates customerId and customerInfo existence)
			System.out.print("Enter Username: ");
			String username = scan.nextLine();
			System.out.print("Enter Password: ");
			String password = scan.nextLine();

			Auth auth = new Auth();
			auth.setCustomers(customer);
			auth.setUserName(username);
			auth.setPassword(password);

			boolean isRegistered = authService.register(auth);
			if (isRegistered) {
				System.out.println("Registration successful.");
			} else {
				System.out.println("Registration failed during authentication step.");
			}

		} catch (InvalidInputException | DatabaseConnectionException e) {
			System.out.println("Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Unexpected error occurred: " + e.getMessage());
		}
	}

	public void handleLoginByRole(String role) {

		try {
			System.out.print("Enter Username: ");
			String username = scan.next();
			System.out.print("Enter Password: ");
			String password = scan.next();

			Auth auth = authService.login(username, password);

			if (auth != null && auth.getCustomers() != null) {
				int customerId = auth.getCustomers().getCustomerId();

				CustomerInfo customerInfo = new CustomerInfo();
				customerInfo = customerInfoService.findByCustomerId(customerId);

				String actualRole = customerInfo.getRole();

				if (actualRole.equalsIgnoreCase(role)) {
					switch (role) {
					case "ADMIN":
						new AdminMenu(filename).displayAdminMenu(customerId);
						break;
					case "CUSTOMER":
						new CustomerMenu(filename).displayCustomerMenu(customerId);
						break;
					case "HOST":
						new HostCustomerMenu(filename).displayHostCustomerMenu(customerId);
						break;
					}
				} else {
					System.out.println("You are not authorized as " + role + ".");
				}
			} else {
				System.out.println("Login failed. Please try again.");
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}
