package com.hexaware.carrental.dao.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hexaware.carrental.dao.CarStatusDao;
import com.hexaware.carrental.dao.VehiclesDao;
import com.hexaware.carrental.entity.CarStatus;
import com.hexaware.carrental.entity.Vehicles;
import com.hexaware.carrental.exception.DatabaseConnectionException;
import com.hexaware.carrental.exception.InvalidInputException;
import com.hexaware.carrental.exception.ResourceNotFoundException;

public class CarStatusDaoImpl implements CarStatusDao {

	private final Connection connection;
	private final VehiclesDao vehiclesDao;

	public CarStatusDaoImpl(Connection connection) throws DatabaseConnectionException {
		if (connection == null) {
			throw new DatabaseConnectionException("Error getting the Connection from the util class");
		}
		this.connection = connection;
		this.vehiclesDao = new VehiclesDaoImpl(connection);
	}

	private CarStatus mapToCarStatus(ResultSet rs) throws SQLException, InvalidInputException {
		CarStatus cs = new CarStatus();
		Vehicles v = vehiclesDao.getVehicleById(rs.getInt("vehicleID"));//maps to the existing vehicle.

		cs.setVehicleStatusId(rs.getInt("vehicleStatusID"));
		cs.setVehicle(v);
		cs.setStatus(rs.getString("status"));

		return cs;
    }
	
	@Override
	public boolean addCarStatus(CarStatus carStatus) {
		String sql = "INSERT INTO CarStatus(status, vehicleID) VALUES(?,?)";
		try (PreparedStatement pst = connection.prepareStatement(sql)) {

			pst.setString(1, carStatus.getStatus());
			pst.setInt(2, carStatus.getVehicle().getVehicleId());

			return pst.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateCarStatus(int vehicleId,String newStatus) {
		String sql = "UPDATE CarStatus SET status=? WHERE vehicleID=?";
		try (PreparedStatement pst = connection.prepareStatement(sql)) {

			pst.setString(1, newStatus);
			pst.setInt(2, vehicleId);

			return pst.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteCarStatus(int vehicleId) {
		String sql = "DELETE FROM CarStatus WHERE vehicleID=?";
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, vehicleId);
			return pst.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public CarStatus findByVehicleId(int vehicleId) {
		String sql = "SELECT vehicleStatusID, status, vehicleID FROM CarStatus WHERE vehicleID=?";
		try (PreparedStatement pst = connection.prepareStatement(sql)) {

			pst.setInt(1, vehicleId);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				return mapToCarStatus(rs);
			} else {
				throw new ResourceNotFoundException("Car Status not found for Vehicle ID: " + vehicleId);
			}

		} catch (SQLException  | ResourceNotFoundException | InvalidInputException e) {
			e.printStackTrace();
		}
		return null;
	}

	

	@Override
	public List<CarStatus> findAll() {
		String sql = "SELECT vehicleStatusID, status, vehicleID FROM CarStatus";
		List<CarStatus> list = new ArrayList<>();
		try (PreparedStatement pst = connection.prepareStatement(sql)) {

			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				list.add(mapToCarStatus(rs));
			}

		} catch (SQLException | InvalidInputException e) {
			e.printStackTrace();
		}
		return list;
	}

}
