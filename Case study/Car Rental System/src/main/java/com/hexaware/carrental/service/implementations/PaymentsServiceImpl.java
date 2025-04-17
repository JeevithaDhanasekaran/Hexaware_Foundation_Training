package com.hexaware.carrental.service.implementations;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hexaware.carrental.dao.LeasesDao;
import com.hexaware.carrental.dao.PaymentsDao;
import com.hexaware.carrental.dao.implementations.LeasesDaoImpl;
import com.hexaware.carrental.dao.implementations.PaymentsDaoImpl;
import com.hexaware.carrental.entity.Leases;
import com.hexaware.carrental.entity.Payments;
import com.hexaware.carrental.entity.Vehicles;
import com.hexaware.carrental.exception.DatabaseConnectionException;
import com.hexaware.carrental.exception.InvalidInputException;
import com.hexaware.carrental.service.PaymentsService;
import com.hexaware.carrental.util.DBconnection;

public class PaymentsServiceImpl implements PaymentsService {

	private PaymentsDao paymentsDao;
	private Connection connection;
	private LeasesDao leasesDao;

	public PaymentsServiceImpl(String fileName) throws DatabaseConnectionException {
		this.connection = DBconnection.getConnection(fileName);
		this.paymentsDao = new PaymentsDaoImpl(connection);
		this.leasesDao = new LeasesDaoImpl(connection);
	}

	@Override
	public boolean addPayment(Payments payment) throws InvalidInputException {
		if (payment == null)
			throw new InvalidInputException("Payment details cannot be null");
		return paymentsDao.addPayment(payment);
	}

	// update this method
	@Override
	public boolean updatePayment(int paymentId, String newStatus) throws InvalidInputException {
		if (newStatus == null || newStatus.trim().isEmpty())
			throw new InvalidInputException("Payment Status cannot be null or empty");
		return paymentsDao.updatePaymentStatus(paymentId, newStatus);
	}

	@Override
	public boolean deletePayment(int paymentId) {
		return paymentsDao.deletePayment(paymentId);
	}

	@Override
	public Payments getPaymentByLeaseId(int leaseId) {
		return paymentsDao.findByLeaseId(leaseId);
	}

	@Override
	public List<Payments> getAllPayments() {
		return new ArrayList<>(paymentsDao.findAllPayments());
	}

	@Override
	public List<Payments> getPaymentsByCustomerId(int customerId) {
		return paymentsDao.findPaymentsByCustomerId(customerId);
	}

	@Override
	public double getTotalRevenue() {
		return paymentsDao.getTotalRevenue();
	}

	@Override
	public double getTotalRevenue(Date startDate, Date endDate) {
		return paymentsDao.getTotalRevenue(startDate, endDate);
	}

	@Override
	public boolean makePayment(int leaseId, BigDecimal enteredAmount, boolean isConfirmed)
			throws InvalidInputException {
		try {
			Leases lease = leasesDao.findById(leaseId);
			Vehicles vehicle = lease.getVehicle();
			BigDecimal dailyRate = vehicle.getDailyRate();

			long days = java.time.temporal.ChronoUnit.DAYS.between(lease.getStartDate().toLocalDate(),
					lease.getEndDate().toLocalDate());

			BigDecimal expectedAmount;
			if ("Monthly".equalsIgnoreCase(lease.getLeaseType())) {
				long months = (days / 30) + ((days % 30 != 0) ? 1 : 0);
				expectedAmount = dailyRate.multiply(BigDecimal.valueOf(30 * months));
			} else {
				expectedAmount = dailyRate.multiply(BigDecimal.valueOf(days));
			}

			if (enteredAmount.compareTo(expectedAmount) != 0 && !isConfirmed) {
				throw new InvalidInputException("Amount mismatch, confirmation required");
			}

			Payments payment = new Payments();
			payment.setLease(lease);
			payment.setAmount(enteredAmount);
			payment.setPaymentStatus("PAID");
			payment.setPaymentDate(new java.sql.Date(System.currentTimeMillis()));

			return paymentsDao.addPayment(payment);
		} catch (Exception e) {
			System.err.println("Error in payment processing: " + e.getMessage());
			return false;
		}
	}

	@Override
	public BigDecimal getExpectedAmountByLeaseId(int leaseId)
			throws InvalidInputException, DatabaseConnectionException {
		// Validate leaseId
		if (leaseId <= 0) {
			throw new InvalidInputException("Invalid Lease ID");
		}
		return paymentsDao.fetchExpectedAmountByLeaseId(leaseId);
	}


}
