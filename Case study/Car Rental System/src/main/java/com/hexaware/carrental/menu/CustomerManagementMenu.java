package com.hexaware.carrental.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.hexaware.carrental.entity.CustomerInfo;
import com.hexaware.carrental.entity.Customers;
import com.hexaware.carrental.exception.DatabaseConnectionException;
import com.hexaware.carrental.exception.InvalidInputException;
import com.hexaware.carrental.service.CustomerInfoService;
import com.hexaware.carrental.service.CustomersService;
import com.hexaware.carrental.service.implementations.CustomerInfoServiceImpl;
import com.hexaware.carrental.service.implementations.CustomersServiceImpl;

public class CustomerManagementMenu {
	private Scanner sc;
	private CustomerInfoService customerInfoService;
	private CustomersService customersService;

	public CustomerManagementMenu(String filename) throws DatabaseConnectionException {
		this.sc = new Scanner(System.in);
		this.customerInfoService = new CustomerInfoServiceImpl(filename);
		this.customersService = new CustomersServiceImpl(filename);
	}

	public void displayCustomerManagementMenu() throws DatabaseConnectionException, InvalidInputException {
		int choice;
		do {
			System.out.println("\n==== Customer Management Menu ====");
			System.out.println("1. Add Customer");
			System.out.println("2. View All Customers");
			System.out.println("3. View Customer by ID");
			System.out.println("4. Update Customer Info");
			System.out.println("5. Delete Customer");
			// view vehicles rented by a particular customer
			System.out.println("6. Back to Admin Menu");

			System.out.println("Enter your choice: ");
			choice = sc.nextInt();

			switch (choice) {
			case 1:
				addCustomer();
				break;
			case 2:
				viewAllCustomers();
				break;
			case 3:
				viewCustomerById();
				break;
			case 4:
				updateCustomerInfo();
				break;
			case 5:
				deleteCustomer();
				break;
			case 6:
				System.out.println("Returning to Admin Menu...");
				break;
			default:
				System.out.println("Invalid Choice! Try again.");
			}

		} while (choice != 6);
	}

	private void addCustomer() {
		try {

			System.out.print("Enter First Name: ");
			String firstName = sc.next();
			System.out.print("Enter Last Name: ");
			String lastName = sc.next();
			System.out.print("Enter you unique License Number: ");
			String licenseNumber = sc.next();

			Customers customer = new Customers();
			customer.setFirstName(firstName);
			customer.setLastName(lastName);
			customer.setLicenseNumber(licenseNumber);

			if (customersService.addCustomer(customer)) {
				System.out.println("Customer added successfully!");

//                int customerId = customer.getCustomerId();

				System.out.print("Enter Email: ");
				String email = sc.next();
				System.out.print("Enter Phone Number: ");
				String phoneNumber = sc.next();
				System.out.print("Enter Address: ");
				sc.nextLine();
				String address = sc.nextLine();
				System.out.print("Enter Role (ADMIN/HOST/CUSTOMER):   ");
				String role = sc.next();

				CustomerInfo customerInfo = new CustomerInfo();
				customerInfo.setCustomers(customer);
				customerInfo.setEmail(email);
				customerInfo.setPhoneNumber(phoneNumber);
				customerInfo.setAddress(address);
				customerInfo.setRole(role);

				if (customerInfoService.addCustomerInfo(customerInfo)) {
					System.out.println("Customer Info added successfully!");
				} else {
					System.out.println("Failed to add Customer Info!");
				}
			} else {
				System.out.println("Failed to add Customer!");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	private void viewAllCustomers() throws InvalidInputException, DatabaseConnectionException {
		viewAllCustomersWithContactInfo();
	}

	// map customers with the customer info data
	public void viewAllCustomersWithContactInfo() throws InvalidInputException, DatabaseConnectionException {

		List<Customers> customers = customersService.getAllCustomers();
		List<CustomerInfo> customerInfoList = customerInfoService.findAll();
		// map using customerID
		Map<Integer, CustomerInfo> customerInfoMap = new HashMap<>();

		if (customers.isEmpty()) {
			System.out.println("No Customers Found.");
			return;
		}

		if (customerInfoList.isEmpty()) {
			System.out.println("No Customer Contact Information Found.");
			return;
		}

		for (CustomerInfo info : customerInfoList) {
			customerInfoMap.put(info.getCustomers().getCustomerId(), info);
		}

		// 1 -> details
		// 2 -> details

		System.out.println("------------Customers with Contact Information -------------");
		boolean foundPair = false;
		for (Customers customer : customers) {
			int customerId = customer.getCustomerId();
			if (customerInfoMap.containsKey(customerId)) {

				CustomerInfo contactInfo = customerInfoMap.get(customerId);

				System.out.println("Customer ID: " + customer.getCustomerId());
				System.out.println("Customer Name: " + customer.getCustomerName());
				System.out.println("Licence Number : " + customer.getLicenseNumber());

				System.out.println("Contact Email: " + contactInfo.getEmail());
				System.out.println("Contact Phone: " + contactInfo.getPhoneNumber());

				System.out.println("-----------------------------------------------------------------------");
				foundPair = true;
			}
		}
		if (!foundPair) {
			System.out.println("No customers found with matching contact information.");
		}
	}

	private void viewCustomerById() throws InvalidInputException {
		System.out.print("Enter Customer ID: ");
		int customerId = sc.nextInt();
		CustomerInfo customer = customerInfoService.findByCustomerId(customerId);
		Customers customers = customersService.findCustomerById(customerId);
		if (customer != null) {
			System.out.println("Customer ID: " + customerId);
			System.out.println("Customer Name: " + customers.getCustomerName());
			System.out.println("Licence Number: " + customers.getLicenseNumber());
			System.out.println("Contact Email: " + customer.getEmail());
			System.out.println("Contact Phone: " + customer.getPhoneNumber());
		} else {
			System.out.println("Customer Not Found.");
		}

	}

	private void updateCustomerInfo() throws InvalidInputException, DatabaseConnectionException {
		System.out.print("Enter Customer ID: ");
		int customerId = sc.nextInt();
		CustomerInfo existing = customerInfoService.findByCustomerId(customerId);
		if (existing == null) {
			System.out.println("Customer Not Found.");
			return;
		}

		System.out.print("Enter New Email: ");
		String email = sc.next();
		System.out.print("Enter New Phone Number: ");
		String phoneNumber = sc.next();
		System.out.print("Enter New Address: ");
		sc.nextLine();
		String address = sc.nextLine();
		System.out.print("Enter New Role (ADMIN/HOST/CUSTOMER): ");// since it is admin submenu, he can control the role
		String role = sc.next();

		existing.setEmail(email);
		existing.setPhoneNumber(phoneNumber);
		existing.setAddress(address);
		existing.setRole(role);

		if (customerInfoService.updateCustomerInfo(existing)) {
			System.out.println("Customer Info Updated Successfully.");
		} else {
			System.out.println("Update Failed.");
		}
	}

	private void deleteCustomer() throws DatabaseConnectionException {
		System.out.print("Enter Customer ID to Delete: ");
		int customerId = sc.nextInt();
		if (customerInfoService.deleteByCustomerId(customerId)) {
			System.out.println("Customer Deleted Successfully.");
		} else {
			System.out.println("Failed to Delete Customer.");
		}
	}

}
