package com.carrental.menu;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.InputMismatchException;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

import com.carrental.entity.Leases;
import com.carrental.entity.Payments;
import com.carrental.exception.DatabaseConnectionException;
import com.carrental.exception.InvalidInputException;
import com.carrental.exception.PaymentRequiredException;
import com.carrental.service.LeasesService;
import com.carrental.service.PaymentsService;
import com.carrental.service.implementations.LeasesServiceImpl;
import com.carrental.service.implementations.PaymentsServiceImpl;

public class LeaseManagementMenu {

	private final Scanner scanner;
	private final LeasesService leasesService;
	private final PaymentsService paymentsService;

	public LeaseManagementMenu(String fileName) throws DatabaseConnectionException {
		scanner = new Scanner(System.in);
		leasesService = new LeasesServiceImpl(fileName);
		paymentsService = new PaymentsServiceImpl(fileName);
	}

	public void displayLeaseManagementMenu() throws InvalidInputException, DatabaseConnectionException {
		while (true) {
			System.out.println("\n-------------------- Lease Management Menu-----------------");
			System.out.println("1. Create Lease");
			System.out.println("2. Return Vehicle (Close Lease)");
			System.out.println("3. View Active Leases");
			System.out.println("4. View Lease History");
			System.out.println("5. Back");

			System.out.print("Choose an option: ");
			int choice = scanner.nextInt();

			switch (choice) {
			case 1:
				createLease();
				break;
			case 2:
				returnVehicle();
				break;
			case 3:
				viewActiveLeases();
				break;
			case 4:
				viewLeaseHistory();
				break;
			case 5:
				System.out.println("Going back.........");
				return;
			default:
				System.out.println("Invalid option, try again!");
			}
		}
	}

	private void viewLeaseHistory() {
		Set<Leases> leaseHistory = leasesService.getCompletedLeases();
		if (leaseHistory.isEmpty()) {
			System.out.println("No lease history found.");
		} else {
			leaseHistory.forEach(System.out::println);
		}
	}

	private void viewActiveLeases() throws DatabaseConnectionException {

		LinkedHashSet<Leases> activeLeases = leasesService.getActiveLeases();
		if (activeLeases.isEmpty()) {
			System.out.println("No active leases found.");
		} else {
			activeLeases.forEach(System.out::println);
		}

	}

	private void returnVehicle() throws InvalidInputException, DatabaseConnectionException {
		try {
			System.out.print("Enter Lease ID to close: ");
			int leaseId = scanner.nextInt();
			scanner.nextLine();

			try {
				boolean closedLease = leasesService.returnVehicle(leaseId);
				if (closedLease)
					System.out.println("Vehicle returned and lease closed successfully!");

			} catch (PaymentRequiredException e) {
				System.err.println(e.getMessage());
				System.out.print("Enter payment amount: ");
				BigDecimal amount = scanner.nextBigDecimal();
				scanner.nextLine();

				Payments payments = new Payments();
				payments.setAmount(amount);
				payments.setLease(leasesService.getLeaseById(leaseId));
				payments.setPaymentStatus("PAID");

				boolean paymentSuccessful = paymentsService.addPayment(payments);
				if (paymentSuccessful) {
					// retry to return Vehicle
					try {
						if (leasesService.returnVehicle(leaseId)) {
							System.out.println("Payment successful. Vehicle returned and lease closed.");

						} else {
							System.out.println("Error: Could not close lease after successful payment."
									+ " Contact Admin for Further processing");
						}
					} catch (Exception e1) {
						System.out.println("Error: Could not close lease after successful payment: " + e1.getMessage());
					}

				} else {
					System.out.println("Payment failed. Vehicle return incomplete.");
				}
			} catch (InvalidInputException e) {
				e.printStackTrace();
				System.out.println("Error: " + e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Failed to return vehicle: " + e.getMessage());
			}

		} catch (InputMismatchException e) {
			e.printStackTrace();
			System.out.println("Invalid input. Please enter a valid number.");
//	        scanner.nextLine(); // Consume the invalid input
		}
	}

	private void createLease() {
		try {
			System.out.print("Enter Customer ID: ");
			int customerId = scanner.nextInt();

			System.out.print("Enter Vehicle ID: ");
			int vehicleId = scanner.nextInt();

			System.out.print("Enter Start Date (YYYY-MM-DD): ");
			Date startDate = Date.valueOf(scanner.next());

			System.out.print("Enter End Date (YYYY-MM-DD): ");
			Date endDate = Date.valueOf(scanner.next());

			System.out.print("Enter Lease Type (Daily/Monthly): ");
			String leaseType = scanner.next();

			boolean lease = leasesService.createLease(customerId, vehicleId, startDate, endDate, leaseType);
			if (lease)
				System.out.println("Lease created successfully: " + lease);
			else
				System.out.println("Failed to create lease");

		} catch (Exception e) {
			System.out.println("Failed to create lease: " + e.getMessage());
		}
	}

}
