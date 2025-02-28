package dao;

import models.User;

public interface ProfileDao {

	User updateProfile(User user);
	User updateProfilePhoto(User user);
}
