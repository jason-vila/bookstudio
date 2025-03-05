package com.bookstudio.dao;

import com.bookstudio.models.User;

public interface ProfileDao {
	User updateProfile(User user);
	User updateProfilePhoto(User user);
}
