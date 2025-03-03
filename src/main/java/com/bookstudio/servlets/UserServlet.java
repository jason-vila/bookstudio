package com.bookstudio.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstudio.models.User;
import com.bookstudio.services.UserService;
import com.google.gson.Gson;

@MultipartConfig(
    maxFileSize = 1048576,
    maxRequestSize = 1048576,
    fileSizeThreshold = 0
)

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    private UserService userService = new UserService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String type = request.getParameter("type");
        switch(type) {
            case "list":
                listUsers(request, response);
                break;
            case "details":
                getUser(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida o método GET no permitido para esta acción");
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         request.setCharacterEncoding("UTF-8");
         response.setCharacterEncoding("UTF-8");
         String type = request.getParameter("type");
         switch(type) {
             case "create":
                 createUser(request, response);
                 break;
             case "update":
                 updateUser(request, response);
                 break;
             case "delete":
                 deleteUser(request, response);
                 break;
             default:
                 response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
                 break;
         }
    }
    
    private void listUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<User> userData = userService.listUsers();
            response.setContentType("application/json");
            Gson gson = new Gson();
            String json = gson.toJson(userData);
            response.getWriter().write(json);
        } catch(Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al listar usuarios");
        }
    }
    
    private void getUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String userId = request.getParameter("userId");
            User user = userService.getUser(userId);
            response.setContentType("application/json");
            Gson gson = new Gson();
            String json = gson.toJson(user);
            response.getWriter().write(json);
        } catch(Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener usuario");
        }
    }
    
    private void createUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User createdUser = userService.createUser(request);
            response.setContentType("application/json");
            Gson gson = new Gson();
            String json = gson.toJson(createdUser);
            response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
        } catch (SQLException e) {
            String errorMessage = e.getMessage();
            if (errorMessage.contains("nombre de usuario")) {
                sendErrorResponse(response, errorMessage, "addUserUsername");
            } else if (errorMessage.contains("correo electrónico")) {
                sendErrorResponse(response, errorMessage, "addUserEmail");
            } else {
                sendErrorResponse(response, "Error al crear el usuario.", "");
            }
        } catch (Exception e) {
            sendErrorResponse(response, "Error inesperado al crear el usuario.", "");
        }
    }
    
    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User updatedUser = userService.updateUser(request);
            userService.updateSession(request, updatedUser);
            byte[] fotoPerfil = updatedUser.getProfilePhoto();
            if (fotoPerfil != null) {
            	updatedUser.setProfilePhotoBase64("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(fotoPerfil));
            }
            updatedUser.setProfilePhoto(null);
            
            response.setContentType("application/json");
            Gson gson = new Gson();
            String json = gson.toJson(updatedUser);
            response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
        } catch(Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\": false, \"message\": \"Error al actualizar el usuario\"}");
        }
    }
    
    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String userId = request.getParameter("userId");
            boolean deletedUser = userService.deleteUser(userId);
            response.setContentType("application/json");
            if (deletedUser) {
                response.getWriter().write("{\"success\": true, \"message\": \"Usuario eliminado exitosamente.\"}");
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"Hubo un error al eliminar el usuario.\"}");
            }
        } catch(Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\": false, \"message\": \"Error al eliminar el usuario.\"}");
        }
    }
    
    private void sendErrorResponse(HttpServletResponse response, String message, String field) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        if (field == null || field.isEmpty()) {
            out.write("{\"success\": false, \"message\": \"" + message + "\"}");
        } else {
            out.write("{\"success\": false, \"message\": \"" + message + "\", \"field\": \"" + field + "\"}");
        }
        out.flush();
    }
}
