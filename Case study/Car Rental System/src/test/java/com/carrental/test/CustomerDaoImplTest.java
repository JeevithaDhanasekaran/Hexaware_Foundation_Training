package com.carrental.test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.carrental.dao.implementations.CustomersDaoImpl;
import com.carrental.entity.Customers;
import com.carrental.exception.CustomerNotFoundException;
import com.carrental.exception.DatabaseConnectionException;

class CustomerDaoImplTest {

	private static Connection connection;
    private CustomersDaoImpl customersDao;
	
	
	@BeforeAll
	static void setUpBeforeClass() throws SQLException {
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CarRentalDB","root","Jeevitha@2127");
        Statement stmt = connection.createStatement();
        stmt.execute("show tables");
	}


	@BeforeEach
	void setUp() throws DatabaseConnectionException {
		customersDao = new CustomersDaoImpl(connection);
	}

	@AfterEach
	void tearDown() throws SQLException {
		Statement stmt = connection.createStatement();
        stmt.execute("DELETE FROM customers");
	}

	@AfterAll
    static void closeConnection() throws SQLException {
        connection.close();
    }

	@Test
    @DisplayName("Should throw CustomerNotFoundException when null customer is passed to addCustomer")
    void testAddCustomerWithNullInput() {
        Exception exception = assertThrows(CustomerNotFoundException.class, () -> {
            customersDao.addCustomer(null);
        });
        assertEquals("Object cannot be null. ID needs to passed", exception.getMessage());
    }
	
	@Test
    @DisplayName("Should add customer successfully with valid input")
    void testAddCustomerSuccessfully() throws Exception {
        Customers customer = new Customers();
        customer.setFirstName("Nina");
        customer.setLastName("Patel");
        customer.setLicenseNumber("AB1234567");

        boolean result = customersDao.addCustomer(customer);
        assertTrue(result);
        assertTrue(customer.getCustomerId() > 0); // ID should be auto-generated
    }
	
	@Test
    @DisplayName("Should throw DatabaseConnectionException when connection is null")
    void testConnectionIsNullThrowsException() {
        Exception exception = assertThrows(DatabaseConnectionException.class, () -> {
            new CustomersDaoImpl(null);
        });
        assertEquals("Failed to connect to database", exception.getMessage());
    }
}
