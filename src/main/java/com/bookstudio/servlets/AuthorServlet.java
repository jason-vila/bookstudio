package com.bookstudio.servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstudio.models.Author;
import com.bookstudio.services.AuthorService;
import com.bookstudio.utils.LocalDateAdapter;
import com.bookstudio.utils.SelectOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@MultipartConfig(
    maxFileSize = 1048576,
    maxRequestSize = 1048576,
    fileSizeThreshold = 0
)

@WebServlet("/AuthorServlet")
public class AuthorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private AuthorService authorService = new AuthorService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         request.setCharacterEncoding("UTF-8");
         response.setCharacterEncoding("UTF-8");
         String type = request.getParameter("type");
         switch (type) {
             case "list":
                 listAuthors(request, response);
                 break;
             case "details":
                 getAuthor(request, response);
                 break;
             case "populateSelects":
                 populateSelects(request, response);
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
         switch (type) {
             case "create":
                 createAuthor(request, response);
                 break;
             case "update":
                 updateAuthor(request, response);
                 break;
             default:
                 response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
                 break;
         }
    }
    
    private void listAuthors(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             List<Author> authorData = authorService.listAuthors();
             response.setContentType("application/json");
             Gson gson = new GsonBuilder()
                             .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                             .create();
             String json = gson.toJson(authorData);
             response.getWriter().write(json);
         } catch(Exception e) {
             e.printStackTrace();
             response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al listar autores");
         }
    }
    
    private void getAuthor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             String authorId = request.getParameter("authorId");
             Author author = authorService.getAuthor(authorId);
             response.setContentType("application/json");
             Gson gson = new GsonBuilder()
                             .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                             .create();
             String json = gson.toJson(author);
             response.getWriter().write(json);
         } catch(Exception e) {
             e.printStackTrace();
             response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener autor");
         }
    }
    
    private void createAuthor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             Author createdAuthor = authorService.createAuthor(request);
             if (createdAuthor.getPhoto() != null) {
                 String fotoBase64 = createdAuthor.getPhoto() != null 
                     ? Base64.getEncoder().encodeToString(createdAuthor.getPhoto()) 
                     : "";
                 createdAuthor.setPhotoBase64("data:image/jpeg;base64," + fotoBase64);
             }
             response.setContentType("application/json");
             Gson gson = new GsonBuilder()
                             .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                             .create();
             String json = gson.toJson(createdAuthor);
             response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
         } catch(Exception e) {
             e.printStackTrace();
             response.setContentType("application/json");
             response.getWriter().write("{\"success\": false, \"message\": \"Error al crear el autor: " + e.getMessage() + "\"}");
         }
    }
    
    private void updateAuthor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             Author updatedAuthor = authorService.updateAuthor(request);
             response.setContentType("application/json");
             Gson gson = new GsonBuilder()
                             .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                             .create();
             if (updatedAuthor != null) {
                 byte[] foto = updatedAuthor.getPhoto();
                 if (foto != null) {
                	 updatedAuthor.setPhotoBase64("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(foto));
                 }
                 updatedAuthor.setPhoto(null);
                 String json = gson.toJson(updatedAuthor);
                 response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
             } else {
                 response.getWriter().write("{\"success\": false, \"message\": \"Error al actualizar el autor\"}");
             }
         } catch(Exception e) {
             e.printStackTrace();
             response.setContentType("application/json");
             response.getWriter().write("{\"success\": false, \"message\": \"Error al actualizar el autor: " + e.getMessage() + "\"}");
         }
    }
    
    private void populateSelects(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             SelectOptions selectOptions = authorService.populateSelects();
             response.setContentType("application/json");
             Gson gson = new GsonBuilder()
                             .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                             .create();
             String json = gson.toJson(selectOptions);
             response.getWriter().write(json);
         } catch(Exception e) {
             e.printStackTrace();
             response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al poblar selects");
         }
    }
}
