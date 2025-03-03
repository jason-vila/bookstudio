package com.bookstudio.dao;

import com.bookstudio.models.User;

public interface AuthDao {
	public User verifyLogin(String username, String password);
}
