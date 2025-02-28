package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConexion {
	public static Connection getConexion() {
	Connection con = null;
	 try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:mysql://localhost/bookstudio_db?useSSL=false&useTimezone=true&serverTimezone=UTC";
		String usr = "root";
		String psw = "rootmysql";
		con = DriverManager.getConnection(url, usr, psw);
	} catch (ClassNotFoundException e) {
		System.out.println("Error >> Driver no Instalado!! " + e.getMessage());
	} catch (SQLException e) {
		System.out.println("Error >> De conexión con la BD " + e.getMessage());
	} catch (Exception e) {
		System.out.println("Error >> General: " + e.getMessage());
	}
	 return con;
	}
}
