package com.bookstudio.dao.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bookstudio.dao.SessionDao;

public class SessionDaoImpl implements SessionDao {

	@Override
	public void saveSessionString(HttpServletRequest request, String key, String value) {
		HttpSession session = request.getSession();
		session.setAttribute(key, value);
	}

	@Override
	public void saveSessionTimeOut(HttpServletRequest request, int time) {
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(time);
	}

	@Override
	public void invalidateSession(HttpServletRequest request) {
		request.getSession().invalidate();
	}
}
