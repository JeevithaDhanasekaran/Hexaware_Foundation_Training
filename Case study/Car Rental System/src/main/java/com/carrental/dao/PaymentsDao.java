package com.carrental.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.carrental.entity.Payments;
import com.carrental.exception.DatabaseConnectionException;

public interface PaymentsDao {

	boolean addPayment(Payments payment);

	Payments findByLeaseId(int leaseId);

	List<Payments> findAllPayments();

	boolean updatePaymentStatus(int paymentId, String status);

	boolean deletePayment(int paymentId);

	List<Payments> findPaymentsByCustomerId(int customerId);

	double getTotalRevenue();

	double getTotalRevenue(Date startDate, Date endDate);

	boolean isPaymentMadeForLease(int leaseId);

	BigDecimal fetchExpectedAmountByLeaseId(int leaseId) throws DatabaseConnectionException;

}
