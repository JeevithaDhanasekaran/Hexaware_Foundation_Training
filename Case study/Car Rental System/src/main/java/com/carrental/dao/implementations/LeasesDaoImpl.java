package com.carrental.dao.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.carrental.dao.CustomersDao;
import com.carrental.dao.LeasesDao;
import com.carrental.dao.VehiclesDao;
import com.carrental.entity.Leases;
import com.carrental.exception.DatabaseConnectionException;
import com.carrental.exception.InvalidInputException;
import com.carrental.exception.LeaseNotFoundException;

public class LeasesDaoImpl implements LeasesDao {

	
	private Connection conn;
	private VehiclesDao vehiclesDao;
	private CustomersDao customersDao;

	public LeasesDaoImpl(Connection conn) throws DatabaseConnectionException {
		if (conn == null) throw new DatabaseConnectionException("Connection is null");
		this.conn = conn;
		this.vehiclesDao = new VehiclesDaoImpl(conn);
		this.customersDao = new CustomersDaoImpl(conn);
	}
	
	private Leases mapToLease(ResultSet rs) throws SQLException, InvalidInputException {
		Leases lease = new Leases();
		lease.setLeaseId(rs.getInt("leaseID"));
		lease.setVehicle(vehiclesDao.getVehicleById(rs.getInt("vehicleID")));
		lease.setCustomer(customersDao.findById(rs.getInt("customerID")));
		lease.setStartDate(rs.getDate("startDate"));
		lease.setEndDate(rs.getDate("endDate"));
		lease.setLeaseType(rs.getString("leaseType"));
		return lease;
	}
	
	
	@Override
	public boolean createLease(Leases lease) throws LeaseNotFoundException {
		
		if(lease == null) {
			throw new LeaseNotFoundException("lease should not be null.");
		}
		String sql = "INSERT INTO Leases(vehicleID, customerID, startDate, endDate, leaseType) VALUES(?,?,?,?,?)";
		try (PreparedStatement pst = conn.prepareStatement(sql)) {
			pst.setInt(1, lease.getVehicle().getVehicleId());
			pst.setInt(2, lease.getCustomer().getCustomerId());
			pst.setDate(3, lease.getStartDate());
			pst.setDate(4, lease.getEndDate());
			pst.setString(5, lease.getLeaseType());
			return pst.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Leases findById(int leaseId) {
		String sql = "SELECT leaseID, vehicleID, customerID, startDate, endDate, leaseType FROM Leases WHERE leaseID=?";
		try (PreparedStatement pst = conn.prepareStatement(sql)) {
			pst.setInt(1, leaseId);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				return mapToLease(rs);
			}
		} catch (SQLException | InvalidInputException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Leases> findAllLeases() {
		String sql = "SELECT leaseID, vehicleID, customerID, startDate, endDate, leaseType FROM Leases";
		List<Leases> list = new ArrayList<>();
		try (PreparedStatement pst = conn.prepareStatement(sql)) {
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				list.add(mapToLease(rs));
			}
		} catch (SQLException | InvalidInputException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Leases> findActiveLeases() {
		String sql = "SELECT leaseID, vehicleID, customerID, startDate, endDate, leaseType FROM Leases WHERE endDate >= CURDATE()";
		List<Leases> list = new ArrayList<>();
		try (PreparedStatement pst = conn.prepareStatement(sql)) {
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				list.add(mapToLease(rs));
			}
		} catch (SQLException | InvalidInputException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Leases> findLeaseHistoryByCustomer(int customerId) {
		String sql = "SELECT leaseID, vehicleID, customerID, startDate, endDate, leaseType FROM Leases WHERE customerID=?";
		List<Leases> list = new ArrayList<>();
		try (PreparedStatement pst = conn.prepareStatement(sql)) {
			pst.setInt(1, customerId);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				list.add(mapToLease(rs));
			}
		} catch (SQLException | InvalidInputException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean updateLease(Leases lease) {
		String sql = "UPDATE Leases SET endDate=?, leaseType=? WHERE leaseID=?";
		try (PreparedStatement pst = conn.prepareStatement(sql)) {
			pst.setDate(1, lease.getEndDate());
			pst.setString(2, lease.getLeaseType());
			pst.setInt(3, lease.getLeaseId());
			return pst.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteLease(int leaseId) {
		String sql = "DELETE FROM Leases WHERE leaseID=?";
		try (PreparedStatement pst = conn.prepareStatement(sql)) {
			pst.setInt(1, leaseId);
			return pst.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean closeLease(int leaseId) throws DatabaseConnectionException {
		String sql = "UPDATE Leases SET endDate = CURRENT_DATE WHERE leaseID = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, leaseId);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseConnectionException("Error while closing lease");
		}
	}

	@Override
	public List<Leases> findAllCompletedLeases() {
		String sql = "SELECT * FROM Leases l " +
                "JOIN Payments p ON l.leaseID = p.leaseID " +
                "WHERE p.paymentStatus = 'PAID'";
		List<Leases> list = new ArrayList<>();
		try (PreparedStatement pst = conn.prepareStatement(sql);
				ResultSet rs = pst.executeQuery()) {
			while (rs.next()) {
			list.add(mapToLease(rs));
			}
		} catch (SQLException | InvalidInputException e) {
			e.printStackTrace();
		}
   return list;
	}

	@Override
	public List<Leases> getLeasesByCustomerID(int customerId) {
		String sql = "SELECT leaseID, vehicleID, customerID, startDate, endDate, leaseType FROM Leases WHERE customerId=?";
		List<Leases> lease=new ArrayList<>(); 
		try (PreparedStatement pst = conn.prepareStatement(sql)) {
			pst.setInt(1, customerId);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				lease.add(mapToLease(rs));
			}
		} catch (SQLException | InvalidInputException e) {
			e.printStackTrace();
		}
		return lease;
	}
}
