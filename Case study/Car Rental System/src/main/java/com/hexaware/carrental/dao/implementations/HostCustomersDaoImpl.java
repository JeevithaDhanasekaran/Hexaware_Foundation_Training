package com.hexaware.carrental.dao.implementations;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hexaware.carrental.dao.HostCustomersDao;
import com.hexaware.carrental.entity.City;
import com.hexaware.carrental.entity.CustomerInfo;
import com.hexaware.carrental.entity.Customers;
import com.hexaware.carrental.entity.HostCustomers;
import com.hexaware.carrental.exception.DatabaseConnectionException;
import com.hexaware.carrental.exception.InvalidInputException;

public class HostCustomersDaoImpl implements HostCustomersDao {

	private Connection connection;

	public HostCustomersDaoImpl(Connection connection) throws DatabaseConnectionException {
		if (connection == null)
			throw new DatabaseConnectionException("Error connecting to the Database");
		this.connection = connection;
	}

	private HostCustomers mapToHostCustomers(ResultSet rs) throws InvalidInputException, SQLException {
		HostCustomers hostCustomer = new HostCustomers();
		hostCustomer.setHostCustomerId(rs.getInt("hostCustomerID"));

		Customers customer = new Customers();
		customer.setCustomerId(rs.getInt("customerID"));
		customer.setFirstName(rs.getString("firstName"));
		customer.setLastName(rs.getString("lastName"));
		customer.setLicenseNumber(rs.getString("licenceNumber"));

		CustomerInfo customerInfo = new CustomerInfo();
		customerInfo.setContactId(rs.getInt("contactID"));
		customerInfo.setCustomers(customer);
		customerInfo.setEmail(rs.getString("email"));
		customerInfo.setPhoneNumber(rs.getString("phoneNumber"));
		customerInfo.setAddress(rs.getString("address"));
		customerInfo.setRole(rs.getString("role"));

		hostCustomer.setCustomerInfo(customerInfo);

		City city = new City();
		city.setCityId(rs.getInt("hostingCityID"));
		city.setCityName(rs.getString("cityName"));
		hostCustomer.setCity(city);

		try {
			hostCustomer.setGstNumber(rs.getString("gstNumber"));
		} catch (InvalidInputException e) {
			e.printStackTrace();
			System.err.println("Error setting GST number during mapping");
		}

		return hostCustomer;
	}

	@Override
	public boolean addHostCustomer(HostCustomers hostCustomer) {
		String sql = "INSERT INTO HostCustomers (customerID, hostingCityID, gstNumber) VALUES (?, ?, ?)";
		try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, hostCustomer.getCustomerInfo().getCustomers().getCustomerId());
			ps.setInt(2, hostCustomer.getCity().getCityId());
			ps.setString(3, hostCustomer.getGstNumber());

			int affectedRows = ps.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Failed to add HostCustomer");
			}

			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					hostCustomer.setHostCustomerId(generatedKeys.getInt(1));
				}
			}
			return true;
		} catch (SQLException e) {
			System.err.println("Error adding HostCustomer: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public HostCustomers findByCustomerId(int customerId) throws InvalidInputException {
		String sql = "SELECT hc.hostCustomerID, hc.customerID, hc.hostingCityID, hc.gstNumber, " +
	             "c.firstName, c.lastName, c.licenceNumber, " +
	             "ci.contactID, ci.email, ci.phoneNumber, ci.address, ci.role, " +
	             " cit.cityName, cit.state, " +
	             "a.username, a.password " +
	             "FROM HostCustomers hc " +
	             "JOIN Customers c ON hc.customerID = c.customerID " +
	             "LEFT JOIN ContactInfo ci ON c.customerID = ci.customerID " +
	             "JOIN City cit ON cit.cityID = hc.hostingCityID " +
	             "LEFT JOIN Auth a ON a.customerID = c.customerID " +
	             "WHERE hc.customerID = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, customerId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return mapToHostCustomers(rs);
			}
		} catch (SQLException e) {
			System.err.println("Error finding HostCustomer by customer ID " + customerId + ": " + e.getMessage());
		}
		return null;
	}

	@Override
	public HostCustomers findById(int hostCustomerId) throws InvalidInputException {
		String sql = "SELECT hc.hostcustomerID, hc.hostingCityID, hc.gstNumber, "
				+ "c.customerID, c.firstName, c.lastName,c.licenceNumber,"
				+ "ci.contactID, ci.email, ci.phoneNumber, ci.address ,ci.role , " + "cit.cityID,cit.cityName "
				+ "FROM HostCustomers hc " + "JOIN Customers c ON hc.customerID = c.customerID "
				+ "JOIN ContactInfo ci ON c.customerID = ci.customerID "
				+ "JOIN City cit ON cit.cityID=hc.hostingCityID " + "WHERE hc.hostCustomerID = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, hostCustomerId);
			
			 ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return mapToHostCustomers(rs);
			}
		} catch (SQLException e) {
			System.err.println("Error finding HostCustomer by ID " + hostCustomerId + ": " + e.getMessage());
		}
		return null;
	}

	@Override
	public List<HostCustomers> findAll() {
		List<HostCustomers> hostCustomersList = new ArrayList<>();
	    String sql = "SELECT hc.hostCustomerID, hc.customerID, hc.hostingCityID, hc.gstNumber, " +
	                 "c.firstName, c.lastName, c.licenceNumber, " +
	                 "ci.contactID, ci.email, ci.phoneNumber, ci.address, ci.role, " +
	                 "a.username, a.password, " +
	                 "city.cityName, city.state " +
	                 "FROM HostCustomers hc " +
	                 "JOIN Customers c ON hc.customerID = c.customerID " +
	                 "JOIN ContactInfo ci ON ci.customerID = c.customerID " +
	                 "JOIN Auth a ON a.customerID = c.customerID " +
	                 "JOIN City city ON hc.hostingCityID = city.cityID";

	    try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
	        while (rs.next()) {
	            hostCustomersList.add(mapToHostCustomers(rs));
	        }
	    } catch (SQLException | InvalidInputException e) {
	        e.printStackTrace();
	        System.err.println("Error fetching all HostCustomers ");
	    }
	    return hostCustomersList;
	}

	@Override
	public List<HostCustomers> findByCityId(int cityId) {
		List<HostCustomers> hosts = new ArrayList<>();

	    String query = "SELECT h.hostCustomerID, h.gstNumber, " +
	                   "cu.customerID, cu.firstName, cu.lastName, cu.licenceNumber, " +
	                   "ci.contactID, ci.email, ci.phoneNumber, ci.address, ci.role, " +
	                   "ct.cityID, ct.cityName, ct.state " +
	                   "FROM HostCustomers h " +
	                   "JOIN Customers cu ON h.customerID = cu.customerID " +
	                   "JOIN ContactInfo ci ON cu.customerID = ci.customerID " +
	                   "JOIN City ct ON h.hostingCityID = ct.cityID " +
	                   "WHERE h.hostingCityID = ?";

	    try (PreparedStatement ps = connection.prepareStatement(query)) {

	        ps.setInt(1, cityId);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            // Create City
	            City city = new City();
	            city.setCityId(rs.getInt("cityID"));
	            city.setCityName(rs.getString("cityName"));
	            city.setState(rs.getString("state"));

	            // Create Customers
	            Customers customer = new Customers();
	            customer.setCustomerId(rs.getInt("customerID"));
	            customer.setFirstName(rs.getString("firstName"));
	            customer.setLastName(rs.getString("lastName"));
	            customer.setLicenseNumber(rs.getString("licenceNumber"));

	            // Create CustomerInfo
	            CustomerInfo customerInfo = new CustomerInfo();
	            customerInfo.setCustomers(customer);
	            customerInfo.setEmail(rs.getString("email"));
	            customerInfo.setPhoneNumber(rs.getString("phoneNumber"));
	            customerInfo.setAddress(rs.getString("address"));
	            customerInfo.setRole(rs.getString("role"));

	            // Create HostCustomers
	            HostCustomers host = new HostCustomers();
	            host.setHostCustomerId(rs.getInt("hostCustomerID"));
	            host.setGstNumber(rs.getString("gstNumber"));
	            host.setCustomerInfo(customerInfo);
	            host.setCity(city);

	            hosts.add(host);
	        }

	    } catch (SQLException | InvalidInputException e) {
	        e.printStackTrace();
	    }

	    return hosts;
	}

	@Override
	public boolean updateHostCustomer(HostCustomers hostCustomer) {
		String sql = "UPDATE HostCustomers SET hostingCityID = ?, gstNumber = ? WHERE customerID = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, hostCustomer.getCity().getCityId());
			ps.setString(2, hostCustomer.getGstNumber());
			ps.setInt(3, hostCustomer.getCustomerInfo().getCustomers().getCustomerId());

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error updating HostCustomer for customer ID "
					+ hostCustomer.getCustomerInfo().getCustomers().getCustomerId());
		}
		return false;
	}

	@Override
	public boolean deleteByHostCustomer(int hostCustomerId) {
		String sql = "DELETE FROM HostCustomers WHERE hostCustomerID = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, hostCustomerId);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Error deleting HostCustomer with ID " + hostCustomerId + ": " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean deleteByCustomerId(int customerId) {
		String sql = "DELETE FROM HostCustomers WHERE customerID = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, customerId);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Error deleting HostCustomer for customer ID " + customerId + ": " + e.getMessage());
		}
		return false;
	}

	@Override
	public BigDecimal getRevenueByHostId(int hostCustomerId) throws DatabaseConnectionException {
		BigDecimal revenue = BigDecimal.ZERO;

	    String sql = "SELECT SUM(p.amount) AS totalRevenue FROM Payments p JOIN Leases l ON p.leaseID = l.leaseID "
	    		+ "JOIN Vehicles v ON l.vehicleID = v.vehicleID JOIN HostVehicle hv ON v.vehicleID = hv.vehicleID "
	    		+ "WHERE hv.hostCustomerID = ?";

	    try (PreparedStatement ps = connection.prepareStatement(sql)) {
	        ps.setInt(1, hostCustomerId);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            revenue = rs.getBigDecimal("totalRevenue");
	        }

	    } catch (SQLException e) {
	        throw new DatabaseConnectionException("Error fetching revenue: " + e.getMessage());
	    }

	    return revenue;
	}

}
