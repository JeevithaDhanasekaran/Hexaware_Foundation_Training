package com.hexaware.carrental.dao.implementations;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hexaware.carrental.dao.VehiclesDao;
import com.hexaware.carrental.entity.Vehicles;
import com.hexaware.carrental.exception.CarNotFoundException;
import com.hexaware.carrental.exception.DatabaseConnectionException;
import com.hexaware.carrental.exception.InvalidInputException;
import com.hexaware.carrental.exception.ResourceNotFoundException;

public class VehiclesDaoImpl implements VehiclesDao {

	private Connection connection;

	public VehiclesDaoImpl(Connection connection) throws DatabaseConnectionException {
		if (connection == null) {
			throw new DatabaseConnectionException("Error connecting to the Database");
		}
		this.connection = connection;
	}

	private Vehicles mapToVehicle(ResultSet rs) throws SQLException, InvalidInputException {
		Vehicles v = new Vehicles();

		v.setVehicleId(rs.getInt("vehicleID"));
		v.setMake(rs.getString("make"));
		v.setModel(rs.getString("model"));
		v.setManufacturingYear(rs.getInt("manufacturingYear"));
		v.setDailyRate(rs.getBigDecimal("dailyRate"));
		v.setPassengerCapacity(rs.getInt("passengerCapacity"));
		v.setEngineCapacity(rs.getDouble("engineCapacity"));
		v.setRating(rs.getDouble("rating"));
		v.setTotalRating(rs.getInt("totalRating"));
		v.setRatingCount(rs.getInt("ratingCount"));

		return v;
	}

	@Override
	public boolean addVehicle(Vehicles vehicle) throws CarNotFoundException {
		
		if(vehicle == null) {
			throw new CarNotFoundException("Vehicle object must be given");
		}

		String sql = "INSERT INTO Vehicles(make, model, manufacturingYear, dailyRate, passengerCapacity, engineCapacity) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pst = connection.prepareStatement(sql)) {

			pst.setString(1, vehicle.getMake());
			pst.setString(2, vehicle.getModel());
			pst.setInt(3, vehicle.getManufacturingYear());
			pst.setBigDecimal(4, vehicle.getDailyRate());
			pst.setInt(5, vehicle.getPassengerCapacity());
			pst.setDouble(6, vehicle.getEngineCapacity());

			return pst.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateVehicle(Vehicles vehicle) {
		String sql = "UPDATE Vehicles SET make=?, model=?, manufacturingYear=?, dailyRate=?, passengerCapacity=?, engineCapacity=? WHERE vehicleID=?";
		try (PreparedStatement pst = connection.prepareStatement(sql)) {

			pst.setString(1, vehicle.getMake());
			pst.setString(2, vehicle.getModel());
			pst.setInt(3, vehicle.getManufacturingYear());
			pst.setBigDecimal(4, vehicle.getDailyRate());
			pst.setInt(5, vehicle.getPassengerCapacity());
			pst.setDouble(6, vehicle.getEngineCapacity());
			pst.setInt(7, vehicle.getVehicleId());

			return pst.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteVehicle(int vehicleId) {
		String sql = "DELETE FROM Vehicles WHERE vehicleID=?";
		try (PreparedStatement pst = connection.prepareStatement(sql)) {

			pst.setInt(1, vehicleId);

			return pst.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Vehicles getVehicleById(int vehicleId) {
		String sql = "SELECT vehicleID, make, model, manufacturingYear, dailyRate, passengerCapacity, engineCapacity, rating, totalRating, ratingCount "
				+ "FROM Vehicles WHERE vehicleID=?";
		try (PreparedStatement pst = connection.prepareStatement(sql)) {

			pst.setInt(1, vehicleId);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				return mapToVehicle(rs);
			} else {
				throw new ResourceNotFoundException("Vehicle not found with ID: " + vehicleId);
			}

		} catch (SQLException | ResourceNotFoundException | InvalidInputException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Vehicles> getAllVehicles() {
		String sql = "SELECT vehicleID, make, model, manufacturingYear, dailyRate, passengerCapacity, engineCapacity, rating, "
				+ "totalRating, ratingCount FROM Vehicles";
		List<Vehicles> vehicleList = new ArrayList<>();

		try (PreparedStatement pst = connection.prepareStatement(sql)) {

			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				vehicleList.add(mapToVehicle(rs));
			}

		} catch (SQLException | InvalidInputException e) {
			e.printStackTrace();
		}
		return vehicleList;
	}

	@Override
	public List<Vehicles> searchVehiclesByMakeModel(String make, String model) {
		String sql = "SELECT * FROM Vehicles WHERE make LIKE ? AND model LIKE ?";
		List<Vehicles> vehicleList = new ArrayList<>();

		try (PreparedStatement pst = connection.prepareStatement(sql)) {

			// append % to use with LIKE operator in SQL
			pst.setString(1, "%" + make + "%");
			pst.setString(2, "%" + model + "%");

			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				vehicleList.add(mapToVehicle(rs));
			}

		} catch (SQLException | InvalidInputException e) {
			e.printStackTrace();
		}
		return vehicleList;
	}

	@Override
	public boolean updateVehicleRating(int vehicleId, double givenRating) {
		try {
			Vehicles v = getVehicleById(vehicleId);

			double newTotalRating = v.getTotalRating() + givenRating;
			int newRatingCount = v.getRatingCount() + 1;
			double newAverageRating = newTotalRating / newRatingCount;

			String sql = "UPDATE Vehicles SET totalRating=?, ratingCount=?, rating=? WHERE vehicleID=?";
			try (PreparedStatement pst = connection.prepareStatement(sql)) {

				pst.setDouble(1, newTotalRating);
				pst.setInt(2, newRatingCount);
				pst.setDouble(3, newAverageRating);
				pst.setInt(4, vehicleId);

				return pst.executeUpdate() > 0;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Vehicles> getVehiclesByCityId(int cityId) {
		List<Vehicles> vehicles = new ArrayList<>();
		String sql = "SELECT v.vehicleID, v.make, v.model, v.manufacturingYear, v.dailyRate, " +
                "v.passengerCapacity, v.engineCapacity, v.rating, v.totalRating, v.ratingCount " +
                "FROM Vehicles v " +
                "JOIN VehicleCity vc ON v.vehicleID = vc.vehicleID " +
                "WHERE vc.cityID = ?";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {

			stmt.setInt(1, cityId);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				vehicles.add(mapToVehicle(rs));
			} 
		} catch (SQLException | InvalidInputException e) {
			System.err.println("Error fetching vehicles");
			e.printStackTrace();
		}
		return vehicles;
	}
	
	@Override
	public List<Vehicles> getAvailableVehicles() throws DatabaseConnectionException {
	    String sql = "SELECT v.vehicleID, v.make, v.model, v.manufacturingYear, v.dailyRate, v.passengerCapacity, v.engineCapacity, v.rating, v.totalRating, v.ratingCount "
	               + "FROM Vehicles v JOIN CarStatus cs ON v.vehicleID = cs.vehicleID "
	               + "WHERE cs.status = 'AVAILABLE'";

	    List<Vehicles> availableVehicles = new ArrayList<>();

	    try (PreparedStatement pst = connection.prepareStatement(sql);
	         ResultSet rs = pst.executeQuery()) {

	        while (rs.next()) {
	            availableVehicles.add(mapToVehicle(rs));
	        }

	    } catch (SQLException | InvalidInputException e) {
	        e.printStackTrace();
	    }

	    return availableVehicles;
	}

	@Override
	public int addVehicleAndReturnId(Vehicles vehicle) {
		String sql = "INSERT INTO Vehicles(make, model, manufacturingYear, dailyRate, passengerCapacity, engineCapacity) VALUES (?, ?, ?, ?, ?, ?)";
	    try (PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	        pst.setString(1, vehicle.getMake());
	        pst.setString(2, vehicle.getModel());
	        pst.setInt(3, vehicle.getManufacturingYear());
	        pst.setBigDecimal(4, vehicle.getDailyRate());
	        pst.setInt(5, vehicle.getPassengerCapacity());
	        pst.setDouble(6, vehicle.getEngineCapacity());

	        int affected = pst.executeUpdate();

	        if (affected > 0) {
	            ResultSet rs = pst.getGeneratedKeys();
	            if (rs.next()) {
	                return rs.getInt(1);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return -1;
	}

	
	@Override
	public BigDecimal getTotalRevenue() throws DatabaseConnectionException {
	    BigDecimal revenue = BigDecimal.ZERO;

	    String query = "SELECT SUM(p.amount) FROM payments p "
	                 + "JOIN leases l ON p.leaseID = l.leaseID "
	                 + "JOIN vehicles v ON l.vehicleID = v.vehicleID";

	    try (PreparedStatement ps = connection.prepareStatement(query);
	         ResultSet rs = ps.executeQuery()) {

	        if (rs.next()) {
	            revenue = rs.getBigDecimal(1);
	            if (revenue == null) {
	                revenue = BigDecimal.ZERO;
	            }
	        }

	    } catch (SQLException e) {
	        throw new DatabaseConnectionException("Failed to fetch total revenue: " + e.getMessage());
	    }

	    return revenue;
	}
	
	@Override
	public int getVehicleIdByLeaseId(int leaseId) throws DatabaseConnectionException {
		try {
			String sql = "SELECT vehicleID FROM Leases WHERE leaseID = ?";
			try (PreparedStatement pst = connection.prepareStatement(sql)) {
				pst.setInt(1, leaseId);
				try (ResultSet rs = pst.executeQuery()) {
					if (rs.next()) {
						return rs.getInt("vehicleID");
					}
				}
			}
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Failed to retrieve vehicle ID for lease");
		}
		throw new DatabaseConnectionException("Vehicle not found for the given lease");
	}

}
