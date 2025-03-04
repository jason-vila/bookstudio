package com.bookstudio.servlets;

import java.io.IOException;
import java.util.Base64;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstudio.dao.AuthDao;
import com.bookstudio.dao.impl.AuthDaoImpl;
import com.bookstudio.dao.impl.SessionDaoImpl;
import com.bookstudio.models.User;
import com.bookstudio.utils.LoginConstants;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private static final int SESSION_TIMEOUT_MINUTES = 15;

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        
        if (type.equals("login")) {
            String username = request.getParameter("txtUsername");
            String password = request.getParameter("txtPassword");
            
            AuthDao authDao = new AuthDaoImpl();
            
            User user = authDao.verifyLogin(username, password);
            
            if (user != null) {
                SessionDaoImpl sessionProject = new SessionDaoImpl();
                sessionProject.saveSessionTimeOut(request, SESSION_TIMEOUT_MINUTES * 60);
                sessionProject.saveSessionString(request, LoginConstants.ID, String.valueOf(user.getUserId()));
                sessionProject.saveSessionString(request, LoginConstants.USERNAME, user.getUsername());
                sessionProject.saveSessionString(request, LoginConstants.FIRSTNAME, user.getFirstName());
                sessionProject.saveSessionString(request, LoginConstants.LASTNAME, user.getLastName());
                sessionProject.saveSessionString(request, LoginConstants.PASSWORD, user.getPassword());
                sessionProject.saveSessionString(request, LoginConstants.EMAIL, user.getEmail());
                sessionProject.saveSessionString(request, LoginConstants.ROLE, user.getRole());

                byte[] photoBytes = user.getProfilePhoto();
                if (photoBytes != null) {
                    String photoBase64 = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(photoBytes);
                    sessionProject.saveSessionString(request, LoginConstants.USER_PROFILE_IMAGE, photoBase64);
                }
                
                request.getSession().setAttribute("user", user);

                response.setContentType("application/json");
                response.getWriter().write("{\"success\": true, \"message\": \"Inicio de sesión exitoso.\"}");
            } else {
            	response.setContentType("application/json");
            	response.getWriter().write("{\"success\": false, \"message\": \"Usuario y/o contraseña incorrectos.\"}");
            }
            
        } else if (type.equals("logout")) {
            SessionDaoImpl sessionProject = new SessionDaoImpl();
            sessionProject.invalidateSession(request);
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }
}
