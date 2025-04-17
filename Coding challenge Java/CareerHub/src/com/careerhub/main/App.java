package com.careerhub.main;

import java.sql.Connection;
import java.util.Scanner;

import com.careerhub.exception.ApplicationDeadlineException;
import com.careerhub.exception.InvalidEmailFormatException;
import com.careerhub.exception.InvalidPhoneNumberFormatException;
import com.careerhub.menu.MainMenu;
import com.careerhub.util.DBConnection;

public class App {
	public static void main(String[] args) throws InvalidEmailFormatException, InvalidPhoneNumberFormatException, ApplicationDeadlineException {
        Scanner scanner = new Scanner(System.in);

        String filename="db.properties";
        Connection conn = DBConnection.getConnection(filename);
        if (conn != null) {
            System.out.println("Connected successfully!");
        } else {
            System.out.println("Failed to connect.");
        }

        MainMenu mainMenu = new MainMenu(filename);
        mainMenu.display();

        scanner.close();
    }
}
