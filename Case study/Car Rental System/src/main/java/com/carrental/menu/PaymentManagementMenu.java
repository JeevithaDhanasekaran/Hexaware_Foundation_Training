package com.carrental.menu;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.carrental.entity.Payments;
import com.carrental.exception.DatabaseConnectionException;
import com.carrental.service.LeasesService;
import com.carrental.service.PaymentsService;
import com.carrental.service.implementations.LeasesServiceImpl;
import com.carrental.service.implementations.PaymentsServiceImpl;

public class PaymentManagementMenu {

	private final PaymentsService paymentsService;
	private final Scanner scanner;
	private final LeasesService leasesService;

	public PaymentManagementMenu(String filename) throws DatabaseConnectionException {
		this.paymentsService = new PaymentsServiceImpl(filename);
		this.scanner = new Scanner(System.in);
		this.leasesService = new LeasesServiceImpl(filename);
	}

	public void displayPaymentManagementMenu() {
		int choice;
		do {
			System.out.println("\n------------- Payment Management Menu ---------------- ");
			System.out.println("1. Record Payment");
			System.out.println("2. View All Payments");
			System.out.println("3. View Payments by Customer ID");
			System.out.println("4. View Total Revenue");
			System.out.println("5. View Revenue Between Dates");
			System.out.println("6. Back");

			System.out.print("Enter your choice: ");
			choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1:
				recordPayment();
				break;
			case 2:
				viewAllPayments();
				break;
			case 3:
				viewPaymentsByCustomerId();
				break;
			case 4:
				viewTotalRevenue();
				break;
			case 5:
				viewRevenueBetweenDates();
				break;
			case 6:
				System.out.println("Returning to previous menu...");
				break;
			default:
				System.out.println("Invalid choice. Try again.");
				break;
			}
		} while (choice != 6);
	}

	private void viewRevenueBetweenDates() {
		try {
			System.out.print("Enter start date (yyyy-MM-dd): ");
			String startStr = scanner.nextLine();
			System.out.print("Enter end date (yyyy-MM-dd): ");
			String endStr = scanner.nextLine();

			DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE; // yyyy-MM-dd formatting
			LocalDate startDateLocal = LocalDate.parse(startStr, formatter);
			LocalDate endDateLocal = LocalDate.parse(endStr, formatter);

			// Convert LocalDate to java.util.Date
			Date startDateUtil = java.sql.Date.valueOf(startDateLocal);
			Date endDateUtil = java.sql.Date.valueOf(endDateLocal);

			double revenue = paymentsService.getTotalRevenue(startDateUtil, endDateUtil);
			System.out.printf("Total Revenue between %s and %s: ₹%.2f%n", startStr, endStr, revenue);// 2 decimal places
		} catch (Exception e) {
			System.out.println("Invalid date format. Please use yyyy-MM-dd ");
		}

	}

	private void viewTotalRevenue() {
		double revenue = paymentsService.getTotalRevenue();
		System.out.printf("Total Revenue: ₹%.2f %n", revenue);// print in 2 decimal places
	}

	private void viewPaymentsByCustomerId() {
		System.out.print("Enter Customer ID: ");
		int customerId = scanner.nextInt();
		List<Payments> list = paymentsService.getPaymentsByCustomerId(customerId);
		if (list.isEmpty()) {
			System.out.println("No payments found for this customer.");
		} else {
			list.forEach(System.out::println);
		}
	}

	private void viewAllPayments() {
		Set<Payments> payments = new LinkedHashSet<>(paymentsService.getAllPayments());// order + no duplicates
		if (payments.isEmpty()) {
			System.out.println("No payments found.");
		} else {
			payments.forEach(System.out::println);
		}
	}

	private void recordPayment() {
		try {
			System.out.print("Enter Lease ID: ");
			int leaseId = scanner.nextInt();

			System.out.print("Enter Payment Amount: ");
			BigDecimal amount = scanner.nextBigDecimal();

			Payments payment = new Payments();
			payment.setLease(leasesService.getLeaseById(leaseId));
			payment.setAmount(amount);
			payment.setPaymentStatus("PAID");

			boolean success = paymentsService.addPayment(payment);
			if (success) {
				System.out.println("Payment Recorded Successfully!");
			} else {
				System.out.println("Failed to process your payment. Contact Admin for Details");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error: " + e.getMessage());
		}

	}

}
