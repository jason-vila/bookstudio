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

import com.bookstudio.models.Publisher;
import com.bookstudio.services.PublisherService;
import com.bookstudio.utils.LocalDateAdapter;
import com.bookstudio.utils.SelectOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@MultipartConfig(
    maxFileSize = 1048576,
    maxRequestSize = 1048576,
    fileSizeThreshold = 0
)

@WebServlet("/PublisherServlet")
public class PublisherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private PublisherService publisherService = new PublisherService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         request.setCharacterEncoding("UTF-8");
         response.setCharacterEncoding("UTF-8");
         String type = request.getParameter("type");
         switch (type) {
             case "list":
                 listPublishers(request, response);
                 break;
             case "details":
                 getPublisher(request, response);
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
                 createPublisher(request, response);
                 break;
             case "update":
                 updatePublisher(request, response);
                 break;
             default:
                 response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
                 break;
         }
    }
    
    private void listPublishers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             List<Publisher> publisherData = publisherService.listPublishers();
             response.setContentType("application/json");
             Gson gson = new Gson();
             String json = gson.toJson(publisherData);
             response.getWriter().write(json);
         } catch(Exception e) {
             e.printStackTrace();
             response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al listar editoriales");
         }
    }
    
    private void getPublisher(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             String publisherId = request.getParameter("publisherId");
             Publisher publisher = publisherService.getPublisher(publisherId);
             response.setContentType("application/json");
             Gson gson = new Gson();
             String json = gson.toJson(publisher);
             response.getWriter().write(json);
         } catch(Exception e) {
             e.printStackTrace();
             response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener editorial");
         }
    }
    
    private void createPublisher(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             Publisher createdPublisher = publisherService.createPublisher(request);
             if (createdPublisher.getPhoto() != null) {
                 String photoBase64 = Base64.getEncoder().encodeToString(createdPublisher.getPhoto());
                 createdPublisher.setPhotoBase64("data:image/jpeg;base64," + photoBase64);
             }
             response.setContentType("application/json");
             Gson gson = new Gson();
             String json = gson.toJson(createdPublisher);
             response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
         } catch(Exception e) {
             e.printStackTrace();
             response.setContentType("application/json");
             response.getWriter().write("{\"success\": false, \"message\": \"Error al crear la editorial: " + e.getMessage() + "\"}");
         }
    }
    
    private void updatePublisher(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             Publisher updatedPublisher = publisherService.updatePublisher(request);
             response.setContentType("application/json");
             Gson gson = new Gson();
             if (updatedPublisher != null) {
                 byte[] photo = updatedPublisher.getPhoto();
                 if (photo != null) {
                	 updatedPublisher.setPhotoBase64("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(photo));
                 }
                 updatedPublisher.setPhoto(null);
                 String json = gson.toJson(updatedPublisher);
                 response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
             } else {
                 response.getWriter().write("{\"success\": false, \"message\": \"Error al actualizar la editorial\"}");
             }
         } catch(Exception e) {
             e.printStackTrace();
             response.setContentType("application/json");
             response.getWriter().write("{\"success\": false, \"message\": \"Error al actualizar la editorial: " + e.getMessage() + "\"}");
         }
    }
    
    private void populateSelects(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             SelectOptions selectOptions = publisherService.populateSelects();
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
