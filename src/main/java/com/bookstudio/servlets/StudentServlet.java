package com.bookstudio.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstudio.models.Student;
import com.bookstudio.services.StudentService;
import com.bookstudio.utils.LocalDateAdapter;
import com.bookstudio.utils.SelectOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet("/StudentServlet")
public class StudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    private StudentService studentService = new StudentService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         request.setCharacterEncoding("UTF-8");
         response.setCharacterEncoding("UTF-8");
         String type = request.getParameter("type");
         switch(type) {
             case "list":
                 listStudents(request, response);
                 break;
             case "details":
                 getStudent(request, response);
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
         switch(type) {
             case "create":
                 createStudent(request, response);
                 break;
             case "update":
                 updateStudent(request, response);
                 break;
             default:
                 response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
                 break;
         }
    }
    
    private void listStudents(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             List<Student> studentData = studentService.listStudents();
             response.setContentType("application/json");
             Gson gson = new GsonBuilder()
                  .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                  .create();
             String json = gson.toJson(studentData);
             response.getWriter().write(json);
         } catch(Exception e) {
             e.printStackTrace();
             response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al listar estudiantes");
         }
    }
    
    private void getStudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             String studentId = request.getParameter("studentId");
             Student student = studentService.getStudent(studentId);
             response.setContentType("application/json");
             Gson gson = new GsonBuilder()
                  .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                  .create();
             String json = gson.toJson(student);
             response.getWriter().write(json);
         } catch(Exception e) {
             e.printStackTrace();
             response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener estudiante");
         }
    }
    
    private void createStudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             Student createdStudent = studentService.createStudent(request);
             response.setContentType("application/json");
             Gson gson = new GsonBuilder()
                  .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                  .create();
             String json = gson.toJson(createdStudent);
             response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
         } catch(SQLException e) {
             String errorMessage = e.getMessage();
             if(errorMessage.contains("DNI")) {
                 sendErrorResponse(response, errorMessage, "addStudentDNI");
             } else if(errorMessage.contains("correo electrónico")) {
                 sendErrorResponse(response, errorMessage, "addStudentEmail");
             } else {
                 sendErrorResponse(response, "Error al crear el estudiante.", "");
             }
         } catch(Exception e) {
             sendErrorResponse(response, "Error inesperado al crear el estudiante.", "");
         }
    }
    
    private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             Student updatedStudent = studentService.updateStudent(request);
             response.setContentType("application/json");
             Gson gson = new GsonBuilder()
                  .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                  .create();
             String json = gson.toJson(updatedStudent);
             response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
         } catch(SQLException e) {
             String errorMessage = e.getMessage();
             if(errorMessage.contains("correo electrónico")) {
                 sendErrorResponse(response, errorMessage, "editStudentEmail");
             } else {
                 sendErrorResponse(response, "Error al actualizar el estudiante.", "");
             }
         } catch(Exception e) {
             sendErrorResponse(response, "Error inesperado al actualizar el estudiante.", "");
         }
    }
    
    private void populateSelects(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             SelectOptions selectOptions = studentService.populateSelects();
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
