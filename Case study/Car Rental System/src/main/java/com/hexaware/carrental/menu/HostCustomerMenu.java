package com.hexaware.carrental.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

import com.hexaware.carrental.entity.CustomerInfo;
import com.hexaware.carrental.entity.HostCustomers;
import com.hexaware.carrental.entity.Vehicles;
import com.hexaware.carrental.exception.DatabaseConnectionException;
import com.hexaware.carrental.service.CustomerInfoService;
import com.hexaware.carrental.service.HostCustomersService;
import com.hexaware.carrental.service.VehiclesService;
import com.hexaware.carrental.service.implementations.CustomerInfoServiceImpl;
import com.hexaware.carrental.service.implementations.HostCustomersServiceImpl;
import com.hexaware.carrental.service.implementations.VehiclesServiceImpl;

public class HostCustomerMenu {

	private HostCustomersService hostService;
	private CustomerInfoService customerInfoService;
	private VehiclesService vehicleService;
	private final Scanner scanner;

	public HostCustomerMenu(String filename) throws DatabaseConnectionException {
		this.hostService = new HostCustomersServiceImpl(filename);
		this.customerInfoService = new CustomerInfoServiceImpl(filename);
		this.vehicleService = new VehiclesServiceImpl(filename);
		this.scanner = new Scanner(System.in);
	}

	public void displayHostCustomerMenu(int customerId) throws DatabaseConnectionException {
		boolean exit = false;
		while (!exit) {
			System.out.println("\n--- Host Customer Menu ---");
			System.out.println("1. View Profile");
			System.out.println("2. Update Contact Info");
			System.out.println("3. Add Vehicle to Host");
			System.out.println("4. View All My Vehicles");
			System.out.println("5. View Revenue");
			System.out.println("6. Delete Account");
			System.out.println("7. Logout");
			System.out.print("Enter your choice: ");

			int choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1:
				viewProfile(customerId);
				break;
			case 2:
				updateProfile(customerId);
				break;
			case 3:
				addVehicleToHost();
				break;
			case 4:
				viewMyVehicles();
				break;
			case 5:
				viewRevenue();
				break;
			case 6:
				deleteAccount();
				break;
			case 7:
				exit = true;
				break;
			default:
				System.out.println("Invalid choice! Please try again.");
			}
		}
	}

	private void deleteAccount() throws DatabaseConnectionException {

		try {
			System.out.print("Enter your Customer ID: ");
			int customerId = scanner.nextInt();
			scanner.nextLine();

			System.out.print(
					"Do you want to keep your customer details? \t Delete only Host Customer record (true/false)? ");
			boolean onlyHost = scanner.nextBoolean();
			scanner.nextLine();

			boolean success = onlyHost ? hostService.deleteHostCustomer(customerId)
					: customerInfoService.deleteByCustomerId(customerId);

			System.out.println(success ? "Deletion successful" : "Deletion failed");
		} catch (Exception e) {
			System.out.println("Error during deletion " + e.getMessage());
		}

	}

	private void viewRevenue() {
		try {
			System.out.print("Enter your Host Customer ID: ");
			int hostCustomerId = scanner.nextInt();

			BigDecimal revenue = hostService.getRevenueByHostId(hostCustomerId);
			System.out.println("Total Revenue: ₹" + revenue);

		} catch (Exception e) {
			System.out.println("Error fetching revenue: " + e.getMessage());
		}
	}

	private void viewMyVehicles() {
		try {
			System.out.print("Enter your Host Customer ID: ");
			int hostCustomerId = scanner.nextInt();

			List<Vehicles> vehicles = hostService.findVehiclesByHostCustomerId(hostCustomerId);

			if (vehicles.isEmpty()) {
				System.out.println("No vehicles hosted.");
			} else {
				System.out.println("Vehicles hosted:");
				vehicles.forEach(System.out::println);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error: " + e.getMessage());
		}
	}

	private void addVehicleToHost() {
		try {
			System.out.print("Enter your Customer ID: ");
			int hostCustomerId = scanner.nextInt();
			scanner.nextLine();

			System.out.print("Make: ");
			String make = scanner.nextLine();

			System.out.print("Model: ");
			String model = scanner.nextLine();

			System.out.print("Manufacturing Year: ");
			int year = scanner.nextInt();
			scanner.nextLine();

			System.out.print("Daily Rate: ");
			BigDecimal rate = scanner.nextBigDecimal();

			System.out.print("Passenger Capacity: ");
			int capacity = scanner.nextInt();

			System.out.print("Engine Capacity (in Litres): ");
			double engine = scanner.nextDouble();
			scanner.nextLine();

			Vehicles vehicle = new Vehicles();
			vehicle.setMake(make);
			vehicle.setModel(model);
			vehicle.setManufacturingYear(year);
			vehicle.setDailyRate(rate);
			vehicle.setPassengerCapacity(capacity);
			vehicle.setEngineCapacity(engine);

			boolean added = hostService.addHostVehicle(hostCustomerId, vehicle.getVehicleId());
			System.out.println(added ? "Vehicle added successfully." : "Failed to add vehicle.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error adding vehicle: " + e.getMessage());
		}
	}

	private void updateProfile(int customerId) {

		try {

			CustomerInfo info = customerInfoService.findByCustomerId(customerId);

			if (info == null) {
				System.out.println("No customer info found.Please update your details using the Customer menu.");
				return;
			}

			System.out.println("Leave blank to keep current value.");

			System.out.print("Enter new Email (" + info.getEmail() + "): ");
			String email = scanner.nextLine();
			if (!email.isBlank())
				info.setEmail(email);

			System.out.print("Enter new Phone Number (" + info.getPhoneNumber() + "): ");
			String phone = scanner.nextLine();
			if (!phone.isBlank())
				info.setPhoneNumber(phone);

			System.out.print("Enter new Address (" + info.getAddress() + "): ");
			String address = scanner.nextLine();
			if (!address.isBlank())
				info.setAddress(address);

			boolean updated = customerInfoService.updateCustomerInfo(info);
			System.out.println(updated ? "Profile updated successfully." : "Update failed.");

		} catch (Exception e) {

			System.out.println("Error while updating profile: " + e.getMessage());
		}
	}

	private void viewProfile(int customerId) {

		try {

			HostCustomers host = hostService.findByCustomerId(customerId);
			CustomerInfo info = customerInfoService.findByCustomerId(customerId);

			if (host != null && info != null) {
				System.out.println("Host Profile:");
				System.out.println("Customer Name: " + info.getCustomers().getCustomerName());
				System.out.println("Email: " + info.getEmail());
				System.out.println("Phone: " + info.getPhoneNumber());
				System.out.println("Address: " + info.getAddress());
				System.out.println("City: " + host.getCity().getCityName());
				System.out.println("GST Number: " + host.getGstNumber());
				System.out.println("Role: " + info.getRole());
			} else {
				System.out.println("Host or Contact Info not found.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error: " + e.getMessage());
		}

	}

}
