package com.hexaware.carrental.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.hexaware.carrental.exception.InvalidInputException;

public class Payments {
	
	//Encapsulation
	private int paymentId;
	private Leases lease; // Association
	private Date paymentDate;
	private BigDecimal amount;
	private String paymentStatus;

	public Payments() {
	}

	public Payments(int paymentId, Leases lease, Date paymentDate, BigDecimal amount, String paymentStatus) {
		this.paymentId = paymentId;
		this.lease = lease;
		this.paymentDate = paymentDate;
		this.amount = amount;
		this.paymentStatus = paymentStatus;
	}

	// Getters
	public int getPaymentId() {
		return paymentId;
	}

	public Leases getLease() {
		return lease;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	// Setters
	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public void setLease(Leases lease) throws InvalidInputException {
		if (lease == null)
			throw new InvalidInputException("Lease details must be provided.");
		this.lease = lease;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public void setAmount(BigDecimal amount) throws InvalidInputException {
		if (amount.compareTo(BigDecimal.ZERO) <= 0)
			throw new InvalidInputException("Amount must be greater than zero.");
		this.amount = amount;
	}

	public void setPaymentStatus(String paymentStatus) throws InvalidInputException {
		if (paymentStatus == null || paymentStatus.trim().isEmpty()) {
            throw new InvalidInputException("Payment status cannot be empty.");
        }
        String trimmedStatus = paymentStatus.trim().toUpperCase();
        if (!trimmedStatus.equals("PENDING") &&  !trimmedStatus.equals("PAID") &&  !trimmedStatus.equals("FAILED")) {
            throw new InvalidInputException("Invalid payment status. Allowed values are: 'PENDING', 'PAID', 'FAILED'.");
        }
        this.paymentStatus = trimmedStatus;
	}

	@Override
	public String toString() {
		return "Payment ID: " + paymentId +
		 ", Lease ID: " + lease.getLeaseId() +
				", Date: " + paymentDate + ", Amount: " + amount + ", Status: " + paymentStatus;
	}

}
