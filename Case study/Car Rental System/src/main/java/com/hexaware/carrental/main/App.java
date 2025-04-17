package com.hexaware.carrental.main;

import com.hexaware.carrental.exception.DatabaseConnectionException;
import com.hexaware.carrental.exception.InvalidInputException;
import com.hexaware.carrental.exception.PaymentRequiredException;
import com.hexaware.carrental.menu.MainMenu;

public class App {
	public static void main(String[] args) throws DatabaseConnectionException, InvalidInputException, PaymentRequiredException {
		String filePath="db.properties";
		try {
			MainMenu mainMenu = new MainMenu(filePath);
            mainMenu.displayMainMenu();
			
		}catch(DatabaseConnectionException | InvalidInputException e) {
			System.out.println("Error initializing application: " + e.getMessage());
		}
		catch (Exception e) {
            e.printStackTrace();
        }
	}
}
