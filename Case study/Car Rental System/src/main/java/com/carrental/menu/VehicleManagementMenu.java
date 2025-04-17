package com.carrental.menu;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.carrental.entity.CarStatus;
import com.carrental.entity.City;
import com.carrental.entity.Vehicles;
import com.carrental.exception.DatabaseConnectionException;
import com.carrental.service.CityService;
import com.carrental.service.VehiclesService;
import com.carrental.service.implementations.CityServiceImpl;
import com.carrental.service.implementations.VehiclesServiceImpl;

public class VehicleManagementMenu {

	private final VehiclesService vehiclesService;
	private final CityService cityService;
	private final Scanner sc;

	public VehicleManagementMenu(String filename) throws DatabaseConnectionException {
		this.vehiclesService = new VehiclesServiceImpl(filename);
		this.cityService = new CityServiceImpl(filename);
		this.sc = new Scanner(System.in);
	}

	public void displayVehicleManagementMenu() {
		while (true) {
			System.out.println("\n------------ VEHICLE MANAGEMENT MENU -------------");
			System.out.println("1. Add Vehicle");
			System.out.println("2. Update Vehicle");
			System.out.println("3. View Vehicle by ID");
			System.out.println("4. View All Vehicles");
			System.out.println("5. View Vehicles by City");
			System.out.println("6. View Rented Vehicles");
			System.out.println("7. Remove Vehicle");
			System.out.println("8. Get Total Revenue");
			System.out.println("9. View City with Highest Lease Frequency");
			System.out.println("10. Back");

			System.out.print("Enter your choice: ");
			int choice = sc.nextInt();

			switch (choice) {
			case 1:
				addVehicle();
				break;
			case 2:
				updateVehicle();
				break;
			case 3:
				viewVehicleById();
				break;
			case 4:
				viewAllVehicles();
				break;
			case 5:
				viewVehiclesByCity();
				break;
			case 6:
				viewRentedVehicles();
				break;
			case 7:
				removeVehicle();
				break;
			case 8:
				getTotalRevenue(); 
				break;
			case 9:
				viewCityWithHighestLeaseFrequency(); 
				break;
			case 10: 
				return;
			default:
				System.out.println("Invalid Choice! Try again.");
			}
		}
	}


	private void viewCityWithHighestLeaseFrequency() {
		try {
			City city = cityService.getCityWithHighestLeaseFrequency();
			System.out.println("City with Highest Leases: " + city);
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	private void getTotalRevenue(){
		try {
	        BigDecimal revenue = vehiclesService.getTotalRevenue();
	        System.out.println("Total Revenue from Leases: ₹" + revenue);
	    } catch (Exception e) {
	        System.out.println("Error: " + e.getMessage());
	    }
	}

	private void removeVehicle() {
		try {
			System.out.print("Enter Vehicle ID to Remove: ");
			int id = sc.nextInt();

			if (vehiclesService.deleteVehicle(id))
				System.out.println("Vehicle Removed Successfully!");
			else
				System.out.println("Failed to Remove Vehicle!");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error: " + e.getMessage());
		}
	}

	private void viewRentedVehicles() {
		List<Vehicles> vehicles = vehiclesService.getVehiclesSortedByModel(0); // or based on conditions
		if (vehicles.isEmpty()) {
			System.out.println("No Vehicles Currently on Lease.");
		} else {
			vehicles.forEach(System.out::println);
		}
	}

	private void viewVehiclesByCity() {
		try {
			sc.nextLine();
			System.out.println("Enter you state: ");
			String state = sc.nextLine();

			System.out.println("Available Cities:");
			List<City> cities = cityService.getCitiesByState(state);
			cities.forEach(System.out::println);

			System.out.print("Enter City ID: ");
			int cityId = sc.nextInt();

			List<Vehicles> vehicles = vehiclesService.getVehiclesByCity(cityId);
			if (vehicles.isEmpty()) {
				System.out.println("No Vehicles Found in This City!");
			} else {
				vehicles.forEach(System.out::println);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error: " + e.getMessage());
		}
	}

	private void viewAllVehicles() {
		List<Vehicles> vehicles = vehiclesService.getAllVehicles();
		if (vehicles.isEmpty()) {
			System.out.println("No Vehicles Found!");
		} else {
			vehicles.forEach(System.out::println);
		}

	}

	private void viewVehicleById() {
		try {
			System.out.print("Enter Vehicle ID: ");
			int id = sc.nextInt();
			Vehicles vehicle = vehiclesService.findVehicleById(id);
			System.out.println(vehicle);
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}

	}

	private void updateVehicle() {
		try {
	        System.out.print("Enter Vehicle ID to Update: ");
	        int id = sc.nextInt();
	        Vehicles vehicle = vehiclesService.findVehicleById(id);
	        if (vehicle == null) {
	            System.out.println("Vehicle Not Found!");
	            return;
	        }

	        sc.nextLine();
	        System.out.print("Enter New Make: ");
	        String make = sc.nextLine();
	        System.out.print("Enter New Model: ");
	        String model = sc.nextLine();

	        vehicle.setMake(make);
	        vehicle.setModel(model);

	        if (vehiclesService.updateVehicle(vehicle)) {
	            System.out.println("Vehicle Updated Successfully!");

	            // Update City
	            System.out.print("Enter your state: ");
	            String state = sc.nextLine();
	            Set<City> cities = new HashSet<>(cityService.getCitiesByState(state));
	            cities.forEach(System.out::println);
	            System.out.print("Enter New City ID to assign vehicle: ");
	            int cityId = sc.nextInt();
	            vehiclesService.updateVehicleCity(vehicle.getVehicleId(), cityId);

	            // Update Car Status
	            System.out.print("Enter New Status (AVAILABLE / NOT_AVAILABLE / MAINTENANCE / BOOKED): ");
	            sc.nextLine();
	            String status = sc.nextLine();
	            if(! vehiclesService.updateCarStatus(vehicle.getVehicleId(), status)) {
	            	System.out.println("Update status failed");
	            }

	            System.out.println("Vehicle Details Updated Successfully!");
	        } else {
	            System.out.println("Vehicle Update Failed!");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("Error: " + e.getMessage());
	    }
	}

	private void addVehicle() {
		try {
			sc.nextLine();
			System.out.print("Enter Make: ");
			String make = sc.nextLine();
			System.out.print("Enter Model: ");
			String model = sc.nextLine();
			System.out.print("Enter Manufacturing Year: ");
			int year = sc.nextInt();
			System.out.print("Enter Daily Rate: ");
			BigDecimal rate = sc.nextBigDecimal();
			System.out.print("Enter Passenger Capacity: ");
			int capacity = sc.nextInt();
			System.out.print("Enter Engine Capacity: ");
			double engine = sc.nextDouble();

			Vehicles vehicle = new Vehicles(make, model, year, rate, capacity, engine);

			if (vehiclesService.addVehicle(vehicle)) {
				System.out.println("Vehicle Added Successfully!");

				sc.nextLine();
				// Assign to city
				System.out.println("Enter you state: ");
				String state = sc.nextLine();

				System.out.println("Available Cities: ");
				Set<City> cities = new HashSet<>(cityService.getCitiesByState(state));
				cities.forEach(System.out::println);

				System.out.print("Enter City ID to assign vehicle: ");
				int cityId = sc.nextInt();

				if (vehiclesService.addVehicleToCity(vehicle, cityId)) {
					System.out.println("Vehicle Assigned to City Successfully!");
				} else {
					System.out.println("Vehicle Assignment to city Failed!");
				}

				// Assign host customer
				System.out.print("Enter Host Customer ID: ");
				int hostCustomerId = sc.nextInt();

				if (vehiclesService.addHostCustomerToVehicle(hostCustomerId, vehicle.getVehicleId())) {
					System.out.println("Vehicle Assigned to Host Customer Successfully!");
				} else {
					System.out.println("Failed to Assign Vehicle to Host Customer!");
				}

				// Set car status
				sc.nextLine();
				System.out.print("Enter Initial Car Status (AVAILABLE, NOT_AVAILABLE, MAINTENANCE, BOOKED): ");
				String status = sc.nextLine();

				if (vehiclesService.addCarStatus(vehicle.getVehicleId(), status)) {
					System.out.println("Car Status Set Successfully!");
				} else {
					System.out.println("Failed to Set Car Status!");
				}

			} else {
				System.out.println("Failed to Add Vehicle!");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error: " + e.getMessage());
		}

	}

}
