package com.carrental.service;

import java.util.List;

import com.carrental.entity.Payments;

public interface PaymentsService {
	boolean addPayment(Payments payment);
    List<Payments> getAllPayments();
    List<Payments> getPaymentsByCustomerId(int customerId);
    double getTotalRevenue();
}
