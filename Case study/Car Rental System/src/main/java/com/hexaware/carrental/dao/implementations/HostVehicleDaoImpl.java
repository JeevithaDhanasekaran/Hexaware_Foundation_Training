package com.hexaware.carrental.dao.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hexaware.carrental.dao.HostCustomersDao;
import com.hexaware.carrental.dao.HostVehicleDao;
import com.hexaware.carrental.dao.VehiclesDao;
import com.hexaware.carrental.entity.HostCustomers;
import com.hexaware.carrental.entity.HostVehicle;
import com.hexaware.carrental.entity.Vehicles;
import com.hexaware.carrental.exception.DatabaseConnectionException;
import com.hexaware.carrental.exception.InvalidInputException;
import com.hexaware.carrental.exception.ResourceNotFoundException;

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

		HostCustomers hc = hostCustomersDao.findById(rs.getInt("hostCustomerID"));
		Vehicles v = vehiclesDao.getVehicleById(rs.getInt("vehicleID"));

		hv.setHostVehicleId(rs.getInt("hostVehicleID"));
		hv.setHostCustomers(hc);
		hv.setVehicle(v);

		return hv;
	}

	@Override
	public boolean addHostVehicle(int hostCustomerId,int vehicleId) {
		String sql = "INSERT INTO HostVehicle(hostCustomerID, vehicleID) VALUES(?,?)";
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, hostCustomerId);
			pst.setInt(2,vehicleId);
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
	public List<Vehicles> findVehiclesByHostCustomerId(int hostCustomerId) throws InvalidInputException {
		String sql = "SELECT hostVehicleID, hostCustomerID, vehicleID FROM HostVehicle WHERE hostCustomerID=?";
	    List<Vehicles> vehicleList = new ArrayList<>();

	    try (PreparedStatement pst = connection.prepareStatement(sql)) {
	        pst.setInt(1, hostCustomerId);
	        ResultSet rs = pst.executeQuery();

	        while (rs.next()) {
	            HostVehicle hostVehicle = mapToHostVehicle(rs);
	            vehicleList.add(hostVehicle.getVehicle());
	        }

	    } catch (SQLException | InvalidInputException e) {
	        e.printStackTrace();
	    }

	    return vehicleList;
	}

	@Override
	public int countVehiclesByHostCustomerId(int hostCustomerId) {
		String sql = "SELECT COUNT(*) FROM HostVehicle WHERE hostID=?";
	    int count = 0;

	    try (PreparedStatement pst = connection.prepareStatement(sql)) {
	        pst.setInt(1, hostCustomerId);
	        ResultSet rs = pst.executeQuery();

	        if (rs.next()) {
	            count = rs.getInt(1); // getting count directly from database (using query)
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return count;
	}

	@Override
	public boolean deleteHostVehicle(int vehicleID) {
		String sql = "DELETE FROM HostVehicle WHERE vehicleID=?";
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, vehicleID);
			return pst.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
