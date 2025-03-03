package com.bookstudio.dao;

import java.sql.SQLException;
import java.util.List;

import com.bookstudio.models.User;

public interface UserDao {
	List<User> listUsers();
	User getUser(String userId);
	User createUser(User user) throws SQLException;
	User updateUser(User user);
	boolean deleteUser(String userId);
}
