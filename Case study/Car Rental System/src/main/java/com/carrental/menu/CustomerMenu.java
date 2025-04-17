package com.carrental.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.carrental.entity.Auth;
import com.carrental.entity.City;
import com.carrental.entity.CustomerInfo;
import com.carrental.entity.Customers;
import com.carrental.entity.Leases;
import com.carrental.entity.Payments;
import com.carrental.entity.Vehicles;
import com.carrental.exception.DatabaseConnectionException;
import com.carrental.exception.InvalidInputException;
import com.carrental.exception.LeaseNotFoundException;
import com.carrental.exception.PaymentRequiredException;
import com.carrental.service.AuthService;
import com.carrental.service.CityService;
import com.carrental.service.CustomerInfoService;
import com.carrental.service.CustomersService;
import com.carrental.service.LeasesService;
import com.carrental.service.PaymentsService;
import com.carrental.service.VehiclesService;
import com.carrental.service.implementations.AuthServiceImpl;
import com.carrental.service.implementations.CityServiceImpl;
import com.carrental.service.implementations.CustomerInfoServiceImpl;
import com.carrental.service.implementations.CustomersServiceImpl;
import com.carrental.service.implementations.LeasesServiceImpl;
import com.carrental.service.implementations.PaymentsServiceImpl;
import com.carrental.service.implementations.VehiclesServiceImpl;

public class CustomerMenu {

	private Scanner scanner;
	private final VehiclesService vehiclesService;
	private final AuthService authService;
	private final LeasesService leasesService;
	private final PaymentsService paymentsService;
	private final CustomerInfoService customerInfoService;
	private final CityService cityService;
	private final CustomersService customersService;

	public CustomerMenu(String filename) throws DatabaseConnectionException {
		scanner = new Scanner(System.in);
		this.vehiclesService = new VehiclesServiceImpl(filename);
		this.leasesService = new LeasesServiceImpl(filename);
		this.paymentsService = new PaymentsServiceImpl(filename);
		this.customerInfoService = new CustomerInfoServiceImpl(filename);
		this.cityService = new CityServiceImpl(filename);
		authService = new AuthServiceImpl(filename);
		customersService = new CustomersServiceImpl(filename);
	}

	public void displayCustomerMenu(int customerId) throws DatabaseConnectionException, InvalidInputException,
			PaymentRequiredException, LeaseNotFoundException {
		boolean exit = false;
		while (!exit) {
			System.out.println("\n--- Customer Menu ---");
			System.out.println("1. View Available Vehicles by City");
			System.out.println("2. Sort / Filter Vehicles");
			System.out.println("3. Create Lease (Daily/Monthly)");
			System.out.println("4. View My Leases");
			System.out.println("5. Make Payment");
			System.out.println("6. View Payment History");
			System.out.println("7. View Profile");
			System.out.println("8. Update Profile");
			System.out.println("9. Return Vehicle");
			System.out.println("10. Logout");
			System.out.print("Enter your choice: ");

			int choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1:
				viewAvailableVehiclesByCity();
				break;
			case 2:
				sortOrFilterVehicles();
				break;
			case 3:
				createLease();
				break;
			case 4:
				viewLeases();
				break;
			case 5:
				makePayment();
				break;
			case 6:
				viewPaymentHistory();
				break;
			case 7:
				viewProfile(customerId);
				break;
			case 8:
				updateProfile(customerId);
				break;
			case 9:
				returnVehicle();
				break;
			case 10:
				exit = true;
				System.out.println("Logging out...");
				break;
			default:
				System.out.println("Invalid choice. Try again.");
			}
		}

	}

	private void returnVehicle() throws DatabaseConnectionException, InvalidInputException, PaymentRequiredException {

		System.out.print("Enter Lease ID to Return Vehicle: ");
		int leaseId = scanner.nextInt();
		if (leasesService.returnVehicle(leaseId))
			System.out.println("Vehicle returned and lease closed.");
		else
			System.out.println("Unable to return vehicle . contact admin.");

	}

	private void updateProfile(int customerId) throws InvalidInputException, DatabaseConnectionException {
		// 1. Fetch existing data
		CustomerInfo info = customerInfoService.findByCustomerId(customerId);
		Auth auth = authService.getAuthByCustomerId(customerId);

		if (info == null || auth == null) {
			System.out.println("Unable to fetch profile details for update.");
			return;
		}

		// 2. Update only allowed fields
		System.out.println("\n--- Update Profile ---");
		System.out.println("Phone Number (non-editable): " + info.getPhoneNumber());

		System.out.print("Enter New Email: ");
		info.setEmail(scanner.nextLine());

		System.out.print("Enter New Address: ");
		info.setAddress(scanner.nextLine());

		System.out.print("Enter New Password: ");
		String newPassword = scanner.nextLine();

		// 3. Update the data in DB
		customerInfoService.updateCustomerInfo(info);
		authService.changePassword(customerId, newPassword);

		System.out.println("Profile updated successfully.");
	}

	private void viewProfile(int customerId) throws InvalidInputException, DatabaseConnectionException {
		// 1. Fetch customer basic details
		Customers customer = customersService.findCustomerById(customerId);
		if (customer == null) {
			System.out.println("Customer not found.");
			return;
		}

		// 2. Fetch contact info
		CustomerInfo info = customerInfoService.findByCustomerId(customerId);
		if (info == null) {
			System.out.println("Customer contact information not found.");
			return;
		}

		// 3. Fetch auth info
		Auth auth = authService.getAuthByCustomerId(customerId);
		if (auth == null) {
			System.out.println("Authentication details not found.");
			return;
		}

		// 4. Display profile details
		System.out.println("\n--- Profile Details ---");
		System.out.println("Name        : " + customer.getFirstName() + " " + customer.getLastName());
		System.out.println("License No. : " + customer.getLicenseNumber());
		System.out.println("Email       : " + info.getEmail());
		System.out.println("Phone       : " + info.getPhoneNumber());
		System.out.println("Address     : " + info.getAddress());
		System.out.println("Role        : " + info.getRole());
		System.out.println("Username    : " + auth.getUserName());
		System.out.println("Password    : " + auth.getPassword());
	}

	private void viewPaymentHistory() {
		System.out.println("Enter your customerId: ");
		int customerId = scanner.nextInt();
		List<Payments> paymentHistory = paymentsService.getPaymentsByCustomerId(customerId);
		paymentHistory.forEach(System.out::println);
	}

	private void makePayment() throws InvalidInputException, DatabaseConnectionException {
		System.out.print("Enter Lease ID to make payment: ");
		int leaseId = scanner.nextInt();

		// Check if payment already made
		Payments existingPayment = paymentsService.getPaymentByLeaseId(leaseId);
		if (existingPayment != null) {
			System.out.println("You have already made a payment for Lease ID: " + leaseId);
			System.out.println("Payment Details:");
			System.out.println(existingPayment);
			return;
		}

		// Get the expected amount to be paid
		BigDecimal expectedAmount = paymentsService.getExpectedAmountByLeaseId(leaseId);
		if (expectedAmount == null) {
			System.out.println("Unable to determine payment amount. Please try again later.");
			return;
		}

		System.out.println("Amount to be paid: " + expectedAmount);
		System.out.print("Do you want to proceed with the payment? (yes/no): ");
		scanner.nextLine();
		String confirm = scanner.nextLine().trim();

		if (!confirm.equalsIgnoreCase("yes")) {
			System.out.println("Payment cancelled.");
			return;
		}

		try {
			boolean result = paymentsService.makePayment(leaseId, expectedAmount, true);
			System.out.println(result ? "Payment Successful!" : "Payment Failed!");
		} catch (InvalidInputException e) {
			System.out.println("Error during payment: " + e.getMessage());
		}
	}

	private void viewLeases() {
		System.out.print("Enter Customer ID: ");
		int customerId = scanner.nextInt();
		Set<Leases> leases = leasesService.getLeaseByCustomerId(customerId);
		leases.forEach(System.out::println);
	}

	private void createLease() throws InvalidInputException, DatabaseConnectionException, LeaseNotFoundException {
		System.out.print("Enter Customer ID: ");
		int customerId = scanner.nextInt();
		System.out.print("Enter Vehicle ID: ");
		int vehicleId = scanner.nextInt();
		scanner.nextLine();
		System.out.print("Enter Lease Type (Daily/Monthly): ");
		String leaseType = scanner.nextLine();
		System.out.print("Enter Start Date (yyyy-mm-dd): ");
		String start = scanner.nextLine();
		System.out.print("Enter End Date (yyyy-mm-dd): ");
		String end = scanner.nextLine();

		leasesService.createLease(customerId, vehicleId, java.sql.Date.valueOf(start), java.sql.Date.valueOf(end),
				leaseType);
		System.out.println("Lease created successfully!");
	}

	private void sortOrFilterVehicles() throws DatabaseConnectionException {
		System.out.print("Enter City Name: ");
		String cityName = scanner.nextLine();
		City city = cityService.getCityByName(cityName);

		System.out.println("Choose sorting option:");
		System.out.println("1. By Model");
		System.out.println("2. By Rating");
		System.out.println("3. By Passenger Capacity");
		int sortChoice = scanner.nextInt();

		List<Vehicles> sortedVehicles;
		switch (sortChoice) {
		case 1:
			sortedVehicles = vehiclesService.getVehiclesSortedByModel(city.getCityId());
			break;
		case 2:
			sortedVehicles = vehiclesService.getVehiclesSortedByRating(city.getCityId());
			break;
		case 3:
			sortedVehicles = vehiclesService.getVehiclesSortedByPassengerCapacity(city.getCityId());
			break;
		default:
			System.out.println("Invalid sorting option.");
			sortedVehicles = List.of();
			break;
		}
		sortedVehicles.forEach(System.out::println);
	}

	private void viewAvailableVehiclesByCity() throws DatabaseConnectionException {
		System.out.print("Enter City Name: ");
		String cityName = scanner.nextLine();
		City city = cityService.getCityByName(cityName);
		List<Vehicles> vehicles = vehiclesService.getVehiclesByCity(city.getCityId());
		vehicles.forEach(System.out::println);

	}
}
