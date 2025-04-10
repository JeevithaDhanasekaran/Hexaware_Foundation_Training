package com.carrental.dao.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.carrental.dao.HostCustomersDao;
import com.carrental.dao.HostVehicleDao;
import com.carrental.dao.VehiclesDao;
import com.carrental.entity.HostCustomers;
import com.carrental.entity.HostVehicle;
import com.carrental.entity.Vehicles;
import com.carrental.exception.DatabaseConnectionException;
import com.carrental.exception.InvalidInputException;
import com.carrental.exception.ResourceNotFoundException;

public class HostVehicleDaoImpl implements HostVehicleDao {

	private Connection connection;
	private HostCustomersDao hostCustomersDao;
	private VehiclesDao vehiclesDao;

	public HostVehicleDaoImpl(Connection conn) throws DatabaseConnectionException {
		if (conn == null)
			throw new DatabaseConnectionException("Connection is Null");

		this.connection = conn;
		this.hostCustomersDao = new HostCustomersDaoImpl(conn);
		this.vehiclesDao = new VehiclesDaoImpl(conn);
	}
	//utility method
	
	private HostVehicle mapToHostVehicle(ResultSet rs) throws SQLException, InvalidInputException {
		HostVehicle hv = new HostVehicle();

		HostCustomers hc = hostCustomersDao.findById(rs.getInt("hostID"));
		Vehicles v = vehiclesDao.getVehicleById(rs.getInt("vehicleID"));

		hv.setHostVehicleId(rs.getInt("hostVehicleID"));
		hv.setHostCustomers(hc);
		hv.setVehicle(v);

		return hv;
	}

	@Override
	public boolean addHostVehicle(HostVehicle hostVehicle) {
		String sql = "INSERT INTO HostVehicle(hostCustomerID, vehicleID) VALUES(?,?)";
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, hostVehicle.getHostCustomers().getHostCustomerId());
			pst.setInt(2, hostVehicle.getVehicle().getVehicleId());
			return pst.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean removeHostVehicle(int hostVehicleId) {
		String sql = "DELETE FROM HostVehicle WHERE hostVehicleID=?";
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, hostVehicleId);
			return pst.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public HostVehicle findByVehicleId(int vehicleId) {
		String sql = "SELECT hostVehicleID, hostID, vehicleID FROM HostVehicle WHERE vehicleID=?";
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, vehicleId);
			ResultSet rs = pst.executeQuery();
			if (rs.next())
				return mapToHostVehicle(rs);
			else
				throw new ResourceNotFoundException("No HostVehicle found for VehicleID: " + vehicleId);
		} catch (SQLException | InvalidInputException | ResourceNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<HostVehicle> findAll() {
		String sql = "SELECT hostVehicleID, hostID, vehicleID FROM HostVehicle";
		List<HostVehicle> list = new ArrayList<>();
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				list.add(mapToHostVehicle(rs));
			}
		} catch (SQLException | InvalidInputException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<HostVehicle> findByHostCustomerId(int hostCustomerId) {
		String sql = "SELECT hostVehicleID, hostID, vehicleID FROM HostVehicle WHERE hostID=?";
		List<HostVehicle> list = new ArrayList<>();
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, hostCustomerId);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				list.add(mapToHostVehicle(rs));
			}
		} catch (SQLException | InvalidInputException e) {
			e.printStackTrace();
		}
		return list;
	}

}
