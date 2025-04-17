package com.hexaware.carrental.dao.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hexaware.carrental.dao.CityDao;
import com.hexaware.carrental.entity.City;
import com.hexaware.carrental.exception.DatabaseConnectionException;
import com.hexaware.carrental.exception.InvalidInputException;
import com.hexaware.carrental.exception.ResourceNotFoundException;

public class CityDaoImpl implements CityDao {

	
	private Connection connection;
	
	public CityDaoImpl(Connection connection) throws DatabaseConnectionException {
		if(connection==null) {
			throw new DatabaseConnectionException("Error getting the Connection from the util class");
		}
		this.connection=connection;
	}
	
	private City mapToCity(ResultSet rs) throws InvalidInputException, SQLException {
		City city = new City();
		city.setCityId(rs.getInt("cityID"));
		city.setCityName(rs.getString("cityName"));
		city.setState("state");;
		return city;
	}
	
	@Override
	public boolean addCity(City city) {
		String sql = "INSERT INTO City(cityName,state) VALUES(?,?)";
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setString(1, city.getCityName());
			pst.setString(2, city.getState());
			return pst.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public City findCityById(int cityId) {
		String sql = "SELECT cityID, cityName FROM City WHERE cityID=?";
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, cityId);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				return mapToCity(rs);
			} else {
				throw new ResourceNotFoundException("City not found with ID: " + cityId);
			}
		} catch (SQLException | ResourceNotFoundException | InvalidInputException e) {
			e.printStackTrace();
		}
		return null;
	}

	

	@Override
	public City findCityByName(String cityName) {
		String sql = "SELECT cityID, cityName FROM City WHERE cityName=?";
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setString(1, cityName);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				return mapToCity(rs);
			} else {
				throw new ResourceNotFoundException("City not found with Name: " + cityName);
			}
		} catch (SQLException | ResourceNotFoundException | InvalidInputException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<City> findAllCity() {
		String sql = "SELECT cityID, cityName FROM City";
		List<City> list = new ArrayList<>();
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				list.add(mapToCity(rs));
			}
		} catch (SQLException | InvalidInputException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean updateCity(City city) {
		String sql = "UPDATE City SET cityName=? WHERE cityID=?";
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setString(1, city.getCityName());
			pst.setInt(2, city.getCityId());
			return pst.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteCity(int cityId) {
		String sql = "DELETE FROM City WHERE cityID=?";
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, cityId);
			return pst.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public City findCityIdByName(String cityName) {
		String sql = "SELECT cityID FROM City WHERE cityName=?";
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setString(1, cityName.toLowerCase().trim());//to ensure case insensitivity
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				return mapToCity(rs);
			} else {
				throw new ResourceNotFoundException("City not found with Name: " + cityName);
			}
		} catch (SQLException | ResourceNotFoundException | InvalidInputException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public City getCityWithHighestLeaseFrequency() throws InvalidInputException, DatabaseConnectionException {
		String sql = "SELECT c.cityID, c.cityName ,c.state FROM Leases l " +
                "JOIN Vehicles v ON l.vehicleID = v.vehicleID " +
                "JOIN VehicleCity vc ON v.vehicleID = vc.vehicleID " +
                "JOIN City c ON vc.cityID = c.cityID " +
                "GROUP BY c.cityID, c.cityName " +
                "ORDER BY COUNT(l.leaseID) DESC " +
                "LIMIT 1";

   try (PreparedStatement pst = connection.prepareStatement(sql);
        ResultSet rs = pst.executeQuery()) {
       if (rs.next()) {
           return mapToCity(rs);
       } else {
           throw new ResourceNotFoundException("No City found with highest lease frequency!");
       }
   } catch (SQLException | ResourceNotFoundException e) {
       e.printStackTrace();
       throw new DatabaseConnectionException("Error finding city with highest lease frequency");
   }
	}

	@Override
	public List<City> getCitiesByState(String state) throws DatabaseConnectionException {
		String sql = "SELECT cityID, cityName, state FROM City WHERE state=?";
		List<City> cities = new ArrayList<>();

		try(PreparedStatement pst = connection.prepareStatement(sql)){
			pst.setString(1, state);
			ResultSet rs = pst.executeQuery();

			while(rs.next()) {
				cities.add(mapToCity(rs));
			}
		}catch(SQLException | InvalidInputException e) {
			throw new DatabaseConnectionException("Error while fetching cities by state"+ e);
		}

		return cities;
		
	}
	
}
