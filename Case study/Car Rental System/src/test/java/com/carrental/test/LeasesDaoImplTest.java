package com.carrental.test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.carrental.dao.LeasesDao;
import com.carrental.dao.implementations.LeasesDaoImpl;
import com.carrental.entity.Customers;
import com.carrental.entity.Leases;
import com.carrental.entity.Vehicles;
import com.carrental.exception.DatabaseConnectionException;
import com.carrental.exception.LeaseNotFoundException;

class LeasesDaoImplTest {

	private static Connection connection;
    private LeasesDao leasesDao;

    @BeforeAll
    static void setupDatabaseConnection() {
        try {
        	connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CarRentalDB","root","Jeevitha@2127");
            Statement stmt = connection.createStatement();
            stmt.execute("show tables");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setup() throws DatabaseConnectionException {
        leasesDao = new LeasesDaoImpl(connection);
    }

    @Test
    void testConnectionIsNull_ThrowsDatabaseConnectionException() {
        Exception exception = assertThrows(DatabaseConnectionException.class, () -> {
            new LeasesDaoImpl(null);
        });
        assertEquals("Connection is null", exception.getMessage());
    }

    @Test
    void testCreateLease_WithNullInput_ThrowsLeaseNotFoundException() {
        assertThrows(LeaseNotFoundException.class, () -> {
            leasesDao.createLease(null);
        });
    }

    @Test
    void testCreateLease_WithValidInput_ReturnsTrue() throws Exception {
        // Create mock vehicle and customer (must exist in your test DB!)
        Vehicles vehicle = new Vehicles();
        vehicle.setVehicleId(1); // Existing vehicle ID in DB
        vehicle.setMake("maruti");
        vehicle.setModel("swift");
        vehicle.setManufacturingYear(2022);
        vehicle.setPassengerCapacity(6);
        vehicle.setEngineCapacity(98);
        vehicle.setDailyRate(new BigDecimal(908.78));
       
        Customers customer = new Customers();
        customer.setCustomerId(1); // Existing customer ID in DB
        customer.setFirstName("Test first name");
        customer.setLastName("Test Last Name");
        customer.setLicenseNumber("AB8778765");

        Leases lease = new Leases();
        lease.setVehicle(vehicle);
        lease.setCustomer(customer);
        lease.setStartDate(Date.valueOf("2025-04-16"));
        lease.setEndDate(Date.valueOf("2025-04-20"));
        lease.setLeaseType("daily");

        boolean isAdded = leasesDao.createLease(lease);
        assertTrue(isAdded, "Lease should be created successfully");
    }

    @AfterAll
    static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

}
