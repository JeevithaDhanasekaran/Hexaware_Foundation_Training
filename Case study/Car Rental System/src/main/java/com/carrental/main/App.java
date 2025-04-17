package com.carrental.main;

import com.carrental.exception.DatabaseConnectionException;
import com.carrental.exception.InvalidInputException;
import com.carrental.exception.PaymentRequiredException;
import com.carrental.menu.MainMenu;

public class App {
	public static void main(String[] args) throws DatabaseConnectionException, InvalidInputException, PaymentRequiredException {
		String filePath="db.properties";
		try {
			MainMenu mainMenu = new MainMenu(filePath);
            mainMenu.displayMainMenu();
			
		}catch(DatabaseConnectionException | InvalidInputException e) {
			System.out.println("Error initializing application: " + e.getMessage());
		}
		catch (Exception e) {//handle unknown errors
            e.printStackTrace();
        }
	}
}
