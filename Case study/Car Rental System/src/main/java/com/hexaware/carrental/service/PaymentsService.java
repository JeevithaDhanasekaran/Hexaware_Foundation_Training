package com.hexaware.carrental.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.hexaware.carrental.entity.Payments;
import com.hexaware.carrental.exception.DatabaseConnectionException;
import com.hexaware.carrental.exception.InvalidInputException;

public interface PaymentsService {

	boolean addPayment(Payments payment) throws InvalidInputException;

	// rarely used by admin , in any of the scenarios mentioned *
	boolean updatePayment(int paymentId, String newStatus) throws InvalidInputException;

	boolean deletePayment(int paymentId);

	// payment for a leaseId
	Payments getPaymentByLeaseId(int leaseId);

	List<Payments> getAllPayments();

	// customer payment history
	List<Payments> getPaymentsByCustomerId(int customerId);

	// total revenue made from beginning day to till day
	double getTotalRevenue();

	// revenue made for particular time period
	double getTotalRevenue(Date startDate, Date endDate);

	boolean makePayment(int leaseId, BigDecimal enteredAmount, boolean isConfirmed) throws InvalidInputException;

	BigDecimal getExpectedAmountByLeaseId(int leaseId) throws InvalidInputException, DatabaseConnectionException;

}

/*
 * Wrong payment status Admin might change from PENDING → PAID manually Failure
 * → Retry Success Update FAILED to PAID Discount/Refund
 */