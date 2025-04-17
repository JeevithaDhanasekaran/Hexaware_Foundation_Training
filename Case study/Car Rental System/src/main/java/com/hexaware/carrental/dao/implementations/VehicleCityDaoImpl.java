package com.hexaware.carrental.dao.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hexaware.carrental.dao.CityDao;
import com.hexaware.carrental.dao.VehicleCityDao;
import com.hexaware.carrental.dao.VehiclesDao;
import com.hexaware.carrental.entity.City;
import com.hexaware.carrental.entity.VehicleCity;
import com.hexaware.carrental.entity.Vehicles;
import com.hexaware.carrental.exception.DatabaseConnectionException;
import com.hexaware.carrental.exception.InvalidInputException;
import com.hexaware.carrental.exception.ResourceNotFoundException;

public class VehicleCityDaoImpl implements VehicleCityDao {

	private Connection conn;
	private VehiclesDao vehiclesDao;
	private CityDao cityDao;

	public VehicleCityDaoImpl(Connection conn) throws DatabaseConnectionException {
		if (conn == null)
			throw new DatabaseConnectionException("Connection is null");

		this.conn = conn;
		this.vehiclesDao = new VehiclesDaoImpl(conn);
		this.cityDao = new CityDaoImpl(conn);
	}

	private VehicleCity mapToVehicleCity(ResultSet rs) throws SQLException, InvalidInputException {
		VehicleCity vc = new VehicleCity();

		Vehicles v = vehiclesDao.getVehicleById(rs.getInt("vehicleID"));
		City c = cityDao.findCityById(rs.getInt("cityID"));

		vc.setVehicleCityId(rs.getInt("vehicleCityID"));
		vc.setVehicle(v);
		vc.setCity(c);

		return vc;
	}

	@Override
	public boolean addVehicleCity(int vehicleId, int cityId) {
		String sql = "INSERT INTO VehicleCity(vehicleID, cityID) VALUES(?, ?)";
		try (PreparedStatement pst = conn.prepareStatement(sql)) {
			pst.setInt(1, vehicleId);
			pst.setInt(2, cityId);
			return pst.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean removeVehicleCity(int vehicleId) {
		String sql = "DELETE FROM VehicleCity WHERE vehicleID=?";
		try (PreparedStatement pst = conn.prepareStatement(sql)) {
			pst.setInt(1, vehicleId);
			return pst.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public VehicleCity findByVehicleId(int vehicleId) {
		String sql = "SELECT vehicleCityID, vehicleID, cityID FROM VehicleCity WHERE vehicleID=?";
		try (PreparedStatement pst = conn.prepareStatement(sql)) {
			pst.setInt(1, vehicleId);
			ResultSet rs = pst.executeQuery();
			if (rs.next())
				return mapToVehicleCity(rs);
			else
				throw new ResourceNotFoundException("VehicleCity Not Found for Vehicle ID: " + vehicleId);
		} catch (SQLException | InvalidInputException | ResourceNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<VehicleCity> findAll() {
		String sql = "SELECT vehicleCityID, vehicleID, cityID FROM VehicleCity";
		List<VehicleCity> list = new ArrayList<>();
		try (PreparedStatement pst = conn.prepareStatement(sql)) {
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				list.add(mapToVehicleCity(rs));
			}
		} catch (SQLException | InvalidInputException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<VehicleCity> findByCityId(int cityId) {
		String sql = "SELECT vehicleCityID, vehicleID, cityID FROM VehicleCity WHERE cityID=?";
		List<VehicleCity> list = new ArrayList<>();
		try (PreparedStatement pst = conn.prepareStatement(sql)) {
			pst.setInt(1, cityId);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				list.add(mapToVehicleCity(rs));
			}
		} catch (SQLException | InvalidInputException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean updateVehicleCity(int vehicleId, int cityId) {
		String sql = "UPDATE VehicleCity SET cityID = ? WHERE vehicleID = ?";
	    try (PreparedStatement pst = conn.prepareStatement(sql)) {
	        pst.setInt(1, cityId);
	        pst.setInt(2, vehicleId);
	        return pst.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

}
