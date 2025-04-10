package com.carrental.dao;

import java.util.List;

import com.carrental.entity.Payments;

public interface PaymentsDao {
	
	boolean addPayment(Payments payment);

	Payments findByLeaseId(int leaseId);

	List<Payments> findAllPayments();

	boolean updatePaymentStatus(int paymentId, String status);

	boolean deletePayment(int paymentId);

	List<Payments> findPaymentsByCustomerId(int customerId);
	
}
