package com.bookstudio.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String URL = "jdbc:mysql://localhost/bookstudio_db?useSSL=false&useTimezone=true&serverTimezone=UTC";
	private static final String USER = "root";
	private static final String PASSWORD = "your_password";

	public static Connection getConexion() {
		Connection con = null;
		try {
			Class.forName(DRIVER);

			con = DriverManager.getConnection(URL, USER, PASSWORD);

		} catch (ClassNotFoundException e) {
			System.out.println("Error: Driver not installed! " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Error: Database connection failed! " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Error: General exception: " + e.getMessage());
		}
		
		return con;
	}
}
