package com.carrental.dao.implementations;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
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

	@Override
	public double getTotalRevenue() {
		String sql = "SELECT SUM(amount) FROM Payments WHERE paymentStatus='PAID'";
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				return rs.getDouble(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public double getTotalRevenue(Date startDate, Date endDate) {
		String sql = "SELECT SUM(amount) FROM Payments WHERE paymentDate BETWEEN ? AND ? AND paymentStatus='PAID'";
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setDate(1, new java.sql.Date(startDate.getTime()));
			pst.setDate(2, new java.sql.Date(endDate.getTime()));
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				return rs.getDouble(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public boolean isPaymentMadeForLease(int leaseId) {
		String sql = "SELECT paymentID, leaseID, paymentDate, amount, paymentStatus FROM Payments WHERE leaseID=?";
		try(PreparedStatement pst = connection.prepareStatement(sql)){
			pst.setInt(1, leaseId);
			ResultSet rs = pst.executeQuery();
			Payments pay;
			if(rs.next()) {
				pay= mapToPayments(rs);
				if(pay.getPaymentStatus().toUpperCase().equals("PAID"))
					return true;
			}
		}catch(SQLException | InvalidInputException e){
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public BigDecimal fetchExpectedAmountByLeaseId(int leaseId) throws DatabaseConnectionException {
		String sql = "SELECT l.leaseType, l.startDate, l.endDate, v.dailyRate "
				+ "FROM Leases l JOIN Vehicles v ON l.vehicleID = v.vehicleID WHERE l.leaseID = ?";

		    try (PreparedStatement ps = connection.prepareStatement(sql)) {

		        ps.setInt(1, leaseId);
		        ResultSet rs = ps.executeQuery();

		        if (rs.next()) {
		            String leaseType = rs.getString("leaseType");
		            LocalDate startDate = rs.getDate("startDate").toLocalDate();
		            LocalDate endDate = rs.getDate("endDate").toLocalDate();
		            BigDecimal dailyRate = rs.getBigDecimal("dailyRate");

		            long days = ChronoUnit.DAYS.between(startDate, endDate.plusDays(1)); // inclusive

		            if (leaseType.equalsIgnoreCase("daily")) {
		                return dailyRate.multiply(BigDecimal.valueOf(days));
		            } else if (leaseType.equalsIgnoreCase("monthly")) {
		                long months = ChronoUnit.MONTHS.between(startDate.withDayOfMonth(1), endDate.withDayOfMonth(1)) + 1;
		                BigDecimal monthlyRate = dailyRate.multiply(BigDecimal.valueOf(30)); // Approximate monthly
		                return monthlyRate.multiply(BigDecimal.valueOf(months));
		            }
		        }

		    } catch (SQLException e) {
		        throw new DatabaseConnectionException("Error calculating expected amount: " + e.getMessage());
		    }

		    return null;
	}

}
