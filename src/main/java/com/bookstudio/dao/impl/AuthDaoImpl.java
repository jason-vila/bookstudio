package com.bookstudio.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bookstudio.dao.AuthDao;
import com.bookstudio.models.User;
import com.bookstudio.utils.DbConnection;

public class AuthDaoImpl implements AuthDao {
	@Override
	public User verifyLogin(String username, String password) {
		User user = null;

		String sql = "SELECT UserID, Username, Email, FirstName, LastName, Password, Role, ProfilePhoto "
				+ "FROM Users " + "WHERE Username = ? AND BINARY Password = ?";

		try (Connection cn = DbConnection.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

			ps.setString(1, username);
			ps.setString(2, password);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					user = new User();
					user.setUserId(rs.getString("UserID"));
					user.setUsername(rs.getString("Username"));
					user.setEmail(rs.getString("Email"));
					user.setFirstName(rs.getString("FirstName"));
					user.setLastName(rs.getString("LastName"));
					user.setPassword(rs.getString("Password"));
					user.setRole(rs.getString("Role"));
					user.setProfilePhoto(rs.getBytes("ProfilePhoto"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return user;
	}
}
