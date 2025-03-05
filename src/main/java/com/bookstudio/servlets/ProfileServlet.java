package com.bookstudio.servlets;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstudio.models.User;
import com.bookstudio.services.ProfileService;
import com.bookstudio.utils.LoginConstants;

@MultipartConfig(
    maxFileSize = 1048576,
    maxRequestSize = 1048576,
    fileSizeThreshold = 0
)

@WebServlet("/ProfileServlet")
public class ProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private ProfileService profileService = new ProfileService();

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String type = request.getParameter("type");
        switch (type) {
            case "updateProfile":
                updateProfile(request, response);
                break;
            case "updateProfilePhoto":
                updateProfilePhoto(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
                break;
        }
    }
	
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String type = request.getParameter("type");
        if ("validatePassword".equals(type)) {
            validatePassword(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida o método GET no permitido para esta acción");
        }
    }
    
    private void updateProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        try {
            User updatedUser = profileService.updateProfile(request);
            if (updatedUser != null) {
                request.getSession().setAttribute(LoginConstants.FIRSTNAME, updatedUser.getFirstName());
                request.getSession().setAttribute(LoginConstants.LASTNAME, updatedUser.getLastName());
                request.getSession().setAttribute(LoginConstants.PASSWORD, updatedUser.getPassword());
                response.getWriter().write("{\"success\": true, \"message\": \"Perfil actualizado con éxito.\"}");
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"Error al actualizar el perfil.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\": false, \"message\": \"Error en el servidor: " + e.getMessage() + "\"}");
        }
    }
    
    private void updateProfilePhoto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        try {
            User updatedUser = profileService.updateProfilePhoto(request);
            if (updatedUser != null) {
                if (updatedUser.getProfilePhoto() != null) {
                    request.getSession().setAttribute(LoginConstants.USER_PROFILE_IMAGE,
                        "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(updatedUser.getProfilePhoto()));
                } else {
                    request.getSession().removeAttribute(LoginConstants.USER_PROFILE_IMAGE);
                }
                response.getWriter().write("{\"success\": true, \"message\": \"Foto actualizada con éxito.\"}");
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"Error al actualizar la foto.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\": false, \"message\": \"Error en el servidor: " + e.getMessage() + "\"}");
        }
    }
    
    private void validatePassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isValid = profileService.validatePassword(request);
        response.setContentType("application/json");
        if (isValid) {
            response.getWriter().write("{\"success\": true}");
        } else {
            response.getWriter().write("{\"success\": false, \"message\": \"La contraseña actual no es correcta.\"}");
        }
    }
}
