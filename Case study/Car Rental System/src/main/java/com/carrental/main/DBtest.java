//package com.carrental.main;
//
//import java.sql.Connection;
//import java.sql.Date;
//import java.util.Arrays;
//import java.util.List;
//
//import com.carrental.dao.CarLeaseRepositoryImpl;
//import com.carrental.entity.Customer;
//import com.carrental.entity.Lease;
//import com.carrental.entity.Vehicle;
//import com.carrental.util.DBconnection;
//
//public class DBtest {
//
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		Connection conn= DBconnection.getConnection("db.properties");
//		if(conn !=null) {
//			System.out.println("Database connection test successful!");
//		}
//		else {
//			System.out.println("Database connection failed");
//		}
//		
//		
//		CarLeaseRepositoryImpl carLeaseRepo = new CarLeaseRepositoryImpl();
//		
//		// Step 1: Add Customers
////        Customer customer1 = new Customer("Riya", "Malik");
////        Customer customer2 = new Customer("Jesinth", "khani");
////        carLeaseRepo.addCustomer(customer1);
////        carLeaseRepo.addCustomer(customer2);
////        System.out.println("Added Customers!");
//        
//     // Step 2: Fetch and display customers
////        List<Customer> customers = carLeaseRepo.listCustomers();
////        customers.forEach(System.out::println);
//        
//     // Step 3: Add Vehicles
//        Vehicle vehicle1 = new Vehicle("Toyota", "Camry", 2022, 50.0, 5, 2.5);	
////        Vehicle vehicle2 = new Vehicle("Honda", "Civic", 2021, 45.0, 5, 2.0);
////        carLeaseRepo.addVehicle(vehicle1);
////        carLeaseRepo.addVehicle(vehicle2);
////        System.out.println("Added Vehicles!");
//		
//		// Step 4: Assign Vehicles to a City
////        carLeaseRepo.assignVehicleToCity(vehicle1.getVehicleID(), 1); 
////        carLeaseRepo.assignVehicleToCity(vehicle2.getVehicleID(), 2); 
////        System.out.println("Assigned Vehicles to Cities!");
//		
//		// Step 5: List Vehicles in a City
////        List<Vehicle> vehiclesInCity = carLeaseRepo.listAvailableVehicles(1);
////        System.out.println("Available Vehicles in City 1: ");
////        vehiclesInCity.forEach(System.out::println);
//
//        // Step 6: Create Lease
//        Lease lease = carLeaseRepo.createLease(1, vehicle1.getVehicleID(), Date.valueOf("2025-04-01"), Date.valueOf("2025-04-07"),"daily");
//        System.out.println("Created Lease: " + lease);
////
////        // Step 7: Process Payment
////        carLeaseRepo.recordPayment(lease, 350.0);
////        System.out.println("Payment Recorded!");
////
////        // Step 8: List Active Leases
////        List<Lease> activeLeases = carLeaseRepo.listActiveLeases();
////        System.out.println("Active Leases: " + activeLeases);
////
////        // Step 9: Return Vehicle
////        carLeaseRepo.returnVehicle(lease.getLeaseID());
////        System.out.println("Vehicle Returned!");
////
////        // Step 10: List Lease History
////        List<Lease> leaseHistory = carLeaseRepo.listLeaseHistory();
////        System.out.println("Lease History: " + leaseHistory);
//		
//	}
//
//}
