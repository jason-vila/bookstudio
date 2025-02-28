package dao;

import models.User;

public interface AuthDao {
	public User verifyLogin(String username, String password);
}
