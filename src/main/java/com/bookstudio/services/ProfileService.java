package com.bookstudio.services;

import javax.servlet.http.Part;

import com.bookstudio.dao.ProfileDao;
import com.bookstudio.dao.UserDao;
import com.bookstudio.dao.impl.ProfileDaoImpl;
import com.bookstudio.dao.impl.UserDaoImpl;
import com.bookstudio.models.User;
import com.bookstudio.utils.LoginConstants;

import javax.servlet.http.HttpServletRequest;

public class ProfileService {
	private ProfileDao profileDao = new ProfileDaoImpl();
	private UserDao userDao = new UserDaoImpl();

	public User updateProfile(HttpServletRequest request) throws Exception {
		String userId = (String) request.getSession().getAttribute(LoginConstants.ID);
		String firstName = request.getParameter("editProfileFirstName");
		String lastName = request.getParameter("editProfileLastName");
		String password = request.getParameter("editProfilePassword");

		if (password == null || password.isEmpty()) {
			password = (String) request.getSession().getAttribute(LoginConstants.PASSWORD);
		}

		User user = new User();
		user.setUserId(userId);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setPassword(password);

		return profileDao.updateProfile(user);
	}

	public User updateProfilePhoto(HttpServletRequest request) throws Exception {
		String userId = (String) request.getSession().getAttribute(LoginConstants.ID);
		byte[] profilePhoto = null;
		String deletePhoto = request.getParameter("deletePhoto");

		if ("true".equals(deletePhoto)) {
			profilePhoto = null;
		} else {
			Part photoPart = request.getPart("editProfilePhoto");
			if (photoPart != null && photoPart.getSize() > 0) {
				profilePhoto = new byte[(int) photoPart.getSize()];
				photoPart.getInputStream().read(profilePhoto);
			} else {
				User currentUser = userDao.getUser(userId);
				if (currentUser != null) {
					profilePhoto = currentUser.getProfilePhoto();
				}
			}
		}

		User user = new User();
		user.setUserId(userId);
		user.setProfilePhoto(profilePhoto);

		return profileDao.updateProfilePhoto(user);
	}

	public boolean validatePassword(HttpServletRequest request) {
		String confirmCurrentPassword = request.getParameter("confirmCurrentPassword").trim();
		String sessionPassword = (String) request.getSession().getAttribute(LoginConstants.PASSWORD);
		
		return sessionPassword != null && sessionPassword.equals(confirmCurrentPassword);
	}
}
