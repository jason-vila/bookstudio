package com.bookstudio.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bookstudio.dao.UserDao;
import com.bookstudio.models.User;
import com.bookstudio.utils.DbConnection;

public class UserDaoImpl implements UserDao {
	@Override
	public List<User> listUsers() {
		List<User> userList = new ArrayList<>();

		String sql = "SELECT UserID, Username, Email, FirstName, LastName, Password, Role, ProfilePhoto FROM Users";

		try (Connection cn = DbConnection.getConexion();
				PreparedStatement ps = cn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				User user = new User();
				user.setUserId(rs.getString("UserID"));
				user.setUsername(rs.getString("Username"));
				user.setEmail(rs.getString("Email"));
				user.setFirstName(rs.getString("FirstName"));
				user.setLastName(rs.getString("LastName"));
				user.setPassword(rs.getString("Password"));
				user.setRole(rs.getString("Role"));
				user.setProfilePhoto(rs.getBytes("ProfilePhoto"));
				userList.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return userList;
	}

	@Override
	public User getUser(String userId) {
		User user = null;

		String sql = "SELECT UserID, Username, Email, FirstName, LastName, Password, Role, ProfilePhoto FROM Users WHERE UserID = ?";

		try (Connection cn = DbConnection.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

			ps.setString(1, userId);
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

	@Override
	public User createUser(User user) throws SQLException {
		String sqlCheckUsername = "SELECT COUNT(*) FROM Users WHERE Username = ?";

		String sqlCheckEmail = "SELECT COUNT(*) FROM Users WHERE Email = ?";

		String sqlInsert = "INSERT INTO Users (Username, Email, FirstName, LastName, Password, Role, ProfilePhoto) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (Connection cn = DbConnection.getConexion()) {
			try (PreparedStatement ps = cn.prepareStatement(sqlCheckUsername)) {
				ps.setString(1, user.getUsername());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next() && rs.getInt(1) > 0) {
						throw new SQLException("El nombre de usuario ingresado ya ha sido registrado.");
					}
				}
			}

			try (PreparedStatement ps = cn.prepareStatement(sqlCheckEmail)) {
				ps.setString(1, user.getEmail());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next() && rs.getInt(1) > 0) {
						throw new SQLException("El correo electrónico ingresado ya ha sido registrado.");
					}
				}
			}

			try (PreparedStatement ps = cn.prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS)) {
				ps.setString(1, user.getUsername());
				ps.setString(2, user.getEmail());
				ps.setString(3, user.getFirstName());
				ps.setString(4, user.getLastName());
				ps.setString(5, user.getPassword());
				ps.setString(6, user.getRole());
				ps.setBytes(7, user.getProfilePhoto());

				int affectedRows = ps.executeUpdate();
				if (affectedRows > 0) {
					try (ResultSet rs = ps.getGeneratedKeys()) {
						if (rs.next()) {
							String generatedId = rs.getString(1);
							user.setUserId(generatedId);
						}
					}
				}
			}
		}
		
		return user;
	}

	@Override
	public User updateUser(User user) {
		String sql = "UPDATE Users SET FirstName = ?, LastName = ?, Password = ?, Role = ?, ProfilePhoto = ? "
				+ "WHERE UserID = ?";

		try (Connection cn = DbConnection.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

			ps.setString(1, user.getFirstName());
			ps.setString(2, user.getLastName());
			ps.setString(3, user.getPassword());
			ps.setString(4, user.getRole());
			ps.setBytes(5, user.getProfilePhoto());
			ps.setString(6, user.getUserId());

			int resultado = ps.executeUpdate();
			if (resultado == 0) {
				System.out.println("No se encontró el usuario con el ID proporcionado.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return user;
	}

	@Override
	public boolean deleteUser(String userId) {
		String sql = "DELETE FROM Users WHERE UserID = ?";

		boolean isDeleted = false;
		try (Connection cn = DbConnection.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
			ps.setString(1, userId);
			int rowsAffected = ps.executeUpdate();
			isDeleted = rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return isDeleted;
	}
}
