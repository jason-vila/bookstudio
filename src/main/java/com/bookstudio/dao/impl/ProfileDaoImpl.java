package com.bookstudio.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.bookstudio.dao.ProfileDao;
import com.bookstudio.models.User;
import com.bookstudio.utils.DbConnection;

public class ProfileDaoImpl implements ProfileDao {
	@Override
	public User updateProfile(User user) {
		String sql = "UPDATE Users SET FirstName = ?, LastName = ?, Password = ? WHERE UserID = ?";

		try (Connection cn = DbConnection.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

			ps.setString(1, user.getFirstName());
			ps.setString(2, user.getLastName());
			ps.setString(3, user.getPassword());
			ps.setString(4, user.getUserId());

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
	public User updateProfilePhoto(User user) {
		String sql = "UPDATE Users SET ProfilePhoto = ? WHERE UserID = ?";

		try (Connection cn = DbConnection.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

			ps.setBytes(1, user.getProfilePhoto());
			ps.setString(2, user.getUserId());

			int resultado = ps.executeUpdate();
			if (resultado == 0) {
				System.out.println("No se encontró el usuario con el ID proporcionado.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return user;
	}
}
