package com.hexaware.carrental.menu;

import java.util.Scanner;

import com.hexaware.carrental.entity.City;
import com.hexaware.carrental.exception.DatabaseConnectionException;
import com.hexaware.carrental.exception.InvalidInputException;
import com.hexaware.carrental.service.CityService;
import com.hexaware.carrental.service.implementations.CityServiceImpl;

public class AdminMenu {
	private Scanner sc;
	private String filename;
	private CityService cityService;

	public AdminMenu(String filename) throws DatabaseConnectionException {
		this.filename = filename;
		sc = new Scanner(System.in);
		cityService = new CityServiceImpl(filename);
	}

	public void displayAdminMenu(int customerId) throws DatabaseConnectionException, InvalidInputException {
		int choice;
		do {
			System.out.println("\n--------- Admin Menu ---------");
			System.out.println("1. Customer Management");
			System.out.println("2. Vehicle Management");
			System.out.println("3. Lease Management");
			System.out.println("4. Payment Management");
			System.out.println("5. Host Customer Management");
			System.out.println("6. Add City");
			System.out.println("7. Logout");

			System.out.print("Enter your choice: ");
			choice = sc.nextInt();

			switch (choice) {
			case 1:
				new CustomerManagementMenu(filename).displayCustomerManagementMenu();
				break;
			case 2:
				new VehicleManagementMenu(filename).displayVehicleManagementMenu();
				break;
			case 3:
				new LeaseManagementMenu(filename).displayLeaseManagementMenu();
				break;
			case 4:
				new PaymentManagementMenu(filename).displayPaymentManagementMenu();
				break;
			case 5:
				new HostCustomerManagementMenu(filename).displayHostCustomerManagementMenu();
				break;
			case 6:
				addCity();
				break;
			case 7:
				System.out.println("Logging out...");
				break;
			default:
				System.out.println("Invalid choice. Try again.");
			}
		} while (choice != 7);
	}

	private void addCity() {
		try {
			sc.nextLine();
			System.out.println("\n--- Add City ---");
			System.out.print("Enter City Name: ");
			String cityName = sc.nextLine();
			sc.nextLine();

			System.out.print("Enter State: ");
			String state = sc.nextLine();

//	        System.out.println("City entered: '" + cityName + "'");

			City city = new City();
			city.setCityName(cityName);
			city.setState(state);

			if (cityService.addCity(city)) {
				System.out.println("City added successfully!");
			} else {
				System.out.println("Failed to add city.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error adding city: " + e.getMessage());
		}

	}
}
