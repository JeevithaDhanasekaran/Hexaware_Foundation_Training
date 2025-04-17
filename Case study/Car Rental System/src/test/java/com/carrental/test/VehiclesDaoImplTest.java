package com.carrental.test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.carrental.dao.implementations.VehiclesDaoImpl;
import com.carrental.entity.Vehicles;
import com.carrental.exception.CarNotFoundException;
import com.carrental.exception.DatabaseConnectionException;
import com.carrental.exception.InvalidInputException;

class VehiclesDaoImplTest {

	private static Connection connection;
    private VehiclesDaoImpl vehiclesDao;

    @BeforeAll
    static void setupDatabase() throws SQLException {
    	connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CarRentalDB","root","Jeevitha@2127");
        Statement stmt = connection.createStatement();
        stmt.execute("show tables");
    }

    @BeforeEach
    void init() throws DatabaseConnectionException {
        vehiclesDao = new VehiclesDaoImpl(connection);
    }

    @AfterEach
    void cleanUp() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM Vehicles");
        }
    }

    @AfterAll
    static void tearDown() throws SQLException {
        connection.close();
    }

    // i) Check if constructor throws DatabaseConnectionException for null connection
    @Test
    void testConstructorThrowsExceptionWhenConnectionIsNull() {
        assertThrows(DatabaseConnectionException.class, () -> new VehiclesDaoImpl(null));
    }

    // ii) Check if addVehicle throws CarNotFoundException when input is null
    @Test
    void testAddVehicleThrowsExceptionForNullInput() {
        assertThrows(CarNotFoundException.class, () -> vehiclesDao.addVehicle(null));
    }

    // iii) Check if valid input adds the vehicle successfully
    @Test
    void testAddVehicleSuccessfully() throws CarNotFoundException {
        Vehicles vehicle = new Vehicles();
        
        try {
        	vehicle.setMake("Toyota");
            vehicle.setModel("Corolla");
            vehicle.setManufacturingYear(2020);
			vehicle.setDailyRate(new BigDecimal("45.99"));
			vehicle.setPassengerCapacity(5);
	        vehicle.setEngineCapacity(1.8);
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}
        boolean added = vehiclesDao.addVehicle(vehicle);
        assertTrue(added);
    }
	
	
}
