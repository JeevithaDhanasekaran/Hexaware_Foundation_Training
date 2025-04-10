package com.carrental.dao.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.carrental.dao.LeasesDao;
import com.carrental.dao.PaymentsDao;
import com.carrental.entity.Payments;
import com.carrental.exception.DatabaseConnectionException;
import com.carrental.exception.InvalidInputException;

public class PaymentsDaoImpl implements PaymentsDao{
	
	private Connection connection;
	private LeasesDao leasesDao;

	public PaymentsDaoImpl(Connection conn) throws DatabaseConnectionException {
		if(conn == null) throw new DatabaseConnectionException("Connection is null");
		this.connection = conn;
		this.leasesDao = new LeasesDaoImpl(connection);
	}

	private Payments mapToPayments(ResultSet rs) throws SQLException, InvalidInputException {
		Payments payment = new Payments();
		payment.setPaymentId(rs.getInt("paymentID"));
		payment.setLease(leasesDao.findById(rs.getInt("leaseID")));
		payment.setPaymentDate(rs.getDate("paymentDate"));
		payment.setAmount(rs.getBigDecimal("amount"));
		payment.setPaymentStatus(rs.getString("paymentStatus"));
		return payment;
	}
	
	@Override
	public boolean addPayment(Payments payment) {
		String sql = "INSERT INTO Payments (leaseID, paymentDate, amount, paymentStatus) VALUES (?, ?, ?, ?)";
		try(PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, payment.getLease().getLeaseId());
			pst.setDate(2, new java.sql.Date(payment.getPaymentDate().getTime())); //check if it goes correct since it has converted from sql.date to util.date
			pst.setBigDecimal(3, payment.getAmount());
			pst.setString(4, payment.getPaymentStatus());
			return pst.executeUpdate() > 0;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Payments findByLeaseId(int leaseId) {
		String sql = "SELECT paymentID, leaseID, paymentDate, amount, paymentStatus FROM Payments WHERE leaseID=?";
		try(PreparedStatement pst = connection.prepareStatement(sql)){
			pst.setInt(1, leaseId);
			ResultSet rs = pst.executeQuery();
			if(rs.next()) return mapToPayments(rs);
		}catch(SQLException | InvalidInputException e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Payments> findAllPayments() {
		List<Payments> list = new ArrayList<>();
		String sql = "SELECT paymentID, leaseID, paymentDate, amount, paymentStatus FROM Payments";
		try(PreparedStatement pst = connection.prepareStatement(sql)){
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				list.add(mapToPayments(rs));
			}
		}catch(SQLException | InvalidInputException e){
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean updatePaymentStatus(int paymentId, String status) {
		String sql = "UPDATE Payments SET paymentStatus=? WHERE paymentID=?";
		try(PreparedStatement pst = connection.prepareStatement(sql)){
			pst.setString(1, status);
			pst.setInt(2, paymentId);
			return pst.executeUpdate() > 0;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deletePayment(int paymentId) {
		String sql = "DELETE FROM Payments WHERE paymentID=?";
		try(PreparedStatement pst = connection.prepareStatement(sql)){
			pst.setInt(1, paymentId);
			return pst.executeUpdate() > 0;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Payments> findPaymentsByCustomerId(int customerId) {
		List<Payments> list = new ArrayList<>();
		String sql = "SELECT p.paymentID, p.leaseID, p.paymentDate, p.amount, p.paymentStatus "
				+ "FROM Payments p JOIN Leases l ON p.leaseID = l.leaseID WHERE l.customerID=?";
		try(PreparedStatement pst = connection.prepareStatement(sql)){
			pst.setInt(1, customerId);
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				list.add(mapToPayments(rs));
			}
		}catch(SQLException | InvalidInputException e){
			e.printStackTrace();
		}
		return list;
	}

}
