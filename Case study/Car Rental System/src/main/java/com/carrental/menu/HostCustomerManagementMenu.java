package com.carrental.menu;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.carrental.entity.City;
import com.carrental.entity.CustomerInfo;
import com.carrental.entity.Customers;
import com.carrental.entity.HostCustomers;
import com.carrental.exception.DatabaseConnectionException;
import com.carrental.service.AuthService;
import com.carrental.service.CityService;
import com.carrental.service.CustomerInfoService;
import com.carrental.service.CustomersService;
import com.carrental.service.HostCustomersService;
import com.carrental.service.implementations.AuthServiceImpl;
import com.carrental.service.implementations.CityServiceImpl;
import com.carrental.service.implementations.CustomerInfoServiceImpl;
import com.carrental.service.implementations.CustomersServiceImpl;
import com.carrental.service.implementations.HostCustomersServiceImpl;

public class HostCustomerManagementMenu {

	private final Scanner scanner;
	private final HostCustomersService hostService;
	private final CustomersService customerService;
	private final CustomerInfoService customerInfoService;
	private final CityService cityService;
	private final AuthService authService;

	public HostCustomerManagementMenu(String filename) throws DatabaseConnectionException {

		this.customerService = new CustomersServiceImpl(filename);
		this.hostService = new HostCustomersServiceImpl(filename);
		this.customerInfoService = new CustomerInfoServiceImpl(filename);
		this.cityService = new CityServiceImpl(filename);
		this.scanner = new Scanner(System.in);
		this.authService = new AuthServiceImpl(filename);
	}

	public void displayHostCustomerManagementMenu() {

		int choice;
		do {
			System.out.println("\n------------------- Host Customer Management ---------------------");
			System.out.println("1. Add Host Customer");
			System.out.println("2. View All Host Customers");
			System.out.println("3. Find Host Customer by ID");
			System.out.println("4. View Host Customers by City");
			System.out.println("5. Update Host Customer");
			System.out.println("6. Delete Host Customer");
			System.out.println("7. Back to Admin Menu");
			System.out.print("Enter your choice: ");
			choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1:
				addHostCustomer();
				break;
			case 2:
				viewAllHostCustomers();
				break;
			case 3:
				findHostCustomerById();
				break;
			case 4:
				viewHostCustomersByCity();
				break;
			case 5:
				updateHostCustomer();
				break;
			case 6:
				deleteHostCustomer();
				break;
			case 7:
				System.out.println("Returning to Admin Menu...");
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
				break;
			}
		} while (choice != 7);

	}

	private void deleteHostCustomer() {
		try {
			System.out.print("Enter Customer ID to Delete from Host Role: ");
			int customerId = scanner.nextInt();
			scanner.nextLine();

			if (hostService.deleteHostCustomer(customerId)) {
				System.out.println("Host customer deleted successfully.");
			} else {
				System.out.println("Failed to delete host customer.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error: " + e.getMessage());
		}

	}

	private void updateHostCustomer() {
		try {
			Map<HostCustomers, CustomerInfo> hostMap = hostService.getAllHostCustomersWithInfo();
			for (Map.Entry<HostCustomers, CustomerInfo> entry : hostMap.entrySet()) {
				HostCustomers host = entry.getKey();
				CustomerInfo info = entry.getValue();
				System.out.println("ID: " + host.getCustomerInfo().getCustomers().getCustomerId() + ", Name: "
						+ host.getCustomerInfo().getCustomers().getFirstName() + " "
						+ host.getCustomerInfo().getCustomers().getLastName() + ", Email: " + info.getEmail()
						+ ", Phone: " + info.getPhoneNumber() + ", City: " + host.getCity().getCityName());
			}

			System.out.print("Enter Customer ID to update: ");
			int customerId = scanner.nextInt();
			scanner.nextLine();

			HostCustomers host = hostService.findByCustomerId(customerId);
			if (host == null) {
				System.out.println("No Host Customer found with ID: " + customerId);
				return;
			}

			System.out.print("Enter new GST Number: ");
			String gst = scanner.nextLine();
			host.setGstNumber(gst);

			System.out.print("Enter new Hosting City Name: ");
			String cityName = scanner.nextLine();
			City city = cityService.getCityByName(cityName);
			host.setCity(city);

			if (hostService.updateHostCustomer(host)) {
				System.out.println("Host customer updated successfully.");
				System.out.println("Updated Details: ");
				System.out.println("Customer ID: " + host.getCustomerInfo().getCustomers().getCustomerId());
				System.out.println("GST Number: " + host.getGstNumber());
				System.out.println("City: " + host.getCity().getCityName() + ", " + host.getCity().getState());
			} else {
				System.out.println("Failed to update host customer.");
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}

	}

	private void viewHostCustomersByCity() {
		try {
			Set<City> cities = cityService.getAllCities();
			if (cities.isEmpty()) {
				System.out.println("No cities available.");
				return;
			}

			System.out.println("Available Cities:");
			cities.forEach(city -> System.out.println(
					"ID: " + city.getCityId() + ", Name: " + city.getCityName() + ", State: " + city.getState()));

			System.out.print("Enter City ID: ");
			int cityId = scanner.nextInt();
			scanner.nextLine();

			List<HostCustomers> hosts = hostService.findByCityId(cityId);
			if (hosts.isEmpty()) {
				System.out.println("No host customers found in the city.");
			} else {
				hosts.forEach(System.out::println);
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}

	}

	private void findHostCustomerById() {
		try {
			System.out.print("Enter Customer ID: ");
			int customerId = scanner.nextInt();
			scanner.nextLine();
			HostCustomers host = hostService.findByCustomerId(customerId);
			if (host != null) {
				System.out.println(host);
			} else {
				System.out.println("No Host Customer found with that Customer ID.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error: " + e.getMessage());
		}

	}

	private void viewAllHostCustomers() {
		try {
			Map<HostCustomers, CustomerInfo> hostMap = hostService.getAllHostCustomersWithInfo();
			if (hostMap.isEmpty()) {
				System.out.println("No host customers found.");
			} else {
				for (Map.Entry<HostCustomers, CustomerInfo> entry : hostMap.entrySet()) {
					HostCustomers host = entry.getKey();
					CustomerInfo info = entry.getValue();

					System.out.println("Host Customer ID: " + host.getHostCustomerId());
					System.out.println("Name: " + info.getCustomers().getCustomerName());
					System.out.println("Email: " + info.getEmail());
					System.out.println("Phone: " + info.getPhoneNumber());
					System.out.println("City: " + host.getCity().getCityName());
					System.out.println("GST Number: " + host.getGstNumber());
				}
			}
		} catch (Exception e) {
			System.out.println("Error while viewing host customers: " + e.getMessage());
		}

	}

	private void addHostCustomer() {
		try {
			System.out
					.println("Are you a new user? Enter 'true' if yes, or 'false' if you already have a customer ID: ");
			boolean isNewCustomer = scanner.nextBoolean();
			scanner.nextLine(); // consume newline

			int customerId;

			if (isNewCustomer) {
				// Register New Customer
				System.out.print("Enter First Name: ");
				String firstName = scanner.nextLine();

				System.out.print("Enter Last Name: ");
				String lastName = scanner.nextLine();

				System.out.print("Enter License Number: ");
				String license = scanner.nextLine();

				Customers customer = new Customers();
				customer.setFirstName(firstName);
				customer.setLastName(lastName);
				customer.setLicenseNumber(license);

				Customers savedCustomer = customerService.addCustomer2(customer);
				customerId = savedCustomer.getCustomerId();

				// Register Contact Info
				System.out.print("Enter Email: ");
				String email = scanner.nextLine();

				System.out.print("Enter Phone Number: ");
				String phone = scanner.nextLine();

				System.out.print("Enter Address: ");
				String address = scanner.nextLine();

				System.out.print("Enter Role (HOST or CUSTOMER): ");
				String role = scanner.nextLine();

				CustomerInfo info = new CustomerInfo();
				info.setCustomers(savedCustomer);
				info.setEmail(email);
				info.setPhoneNumber(phone);
				info.setAddress(address);
				info.setRole(role);

				customerInfoService.addCustomerInfo(info);

				System.out.println("Customer registered successfully. Proceeding to host registration...");
			} else {
				System.out.print("Enter your existing Customer ID: ");
				customerId = scanner.nextInt();
				scanner.nextLine();
			}

			// host registration- common for both
			System.out.print("Enter Hosting City Name: ");
			String cityName = scanner.nextLine();
			City city = cityService.getCityByName(cityName);

			System.out.print("Enter GST Number: ");
			String gst = scanner.nextLine();

			CustomerInfo customerInfo = customerInfoService.findByCustomerId(customerId);
			HostCustomers host = new HostCustomers();
			host.setCustomerInfo(customerInfo);
			host.setCity(city);
			host.setGstNumber(gst);

			if (hostService.addHostCustomer(host)) {
				System.out.println("Host customer added successfully.");
			} else {
				System.out.println("Failed to add host customer.");
			}

		} catch (Exception e) {
			System.out.println("Error during host registration: " + e.getMessage());
		}
	}

}
