package com.bookstudio.servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstudio.models.Course;
import com.bookstudio.services.CourseService;
import com.bookstudio.utils.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet("/CourseServlet")
public class CourseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    private CourseService courseService = new CourseService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         request.setCharacterEncoding("UTF-8");
         response.setCharacterEncoding("UTF-8");
         String type = request.getParameter("type");
         switch (type) {
             case "list":
                 listCourses(request, response);
                 break;
             case "details":
                 getCourse(request, response);
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
                 createCourse(request, response);
                 break;
             case "update":
                 updateCourse(request, response);
                 break;
             default:
                 response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
                 break;
         }
    }
    
    private void listCourses(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             List<Course> courseData = courseService.listCourses();
             response.setContentType("application/json");
             Gson gson = new Gson();
             String json = gson.toJson(courseData);
             response.getWriter().write(json);
         } catch (Exception e) {
             e.printStackTrace();
             response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al listar cursos");
         }
    }
    
    private void getCourse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             String courseId = request.getParameter("courseId");
             Course course = courseService.getCourse(courseId);
             response.setContentType("application/json");
             Gson gson = new Gson();
             String json = gson.toJson(course);
             response.getWriter().write(json);
         } catch (Exception e) {
             e.printStackTrace();
             response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener curso");
         }
    }
    
    private void createCourse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             Course createdCourse = courseService.createCourse(request);
             response.setContentType("application/json");
             Gson gson = new Gson();
             String json = gson.toJson(createdCourse);
             response.getWriter().write(json);
         } catch (Exception e) {
             e.printStackTrace();
             response.setContentType("application/json");
             response.getWriter().write("{\"success\": false, \"message\": \"Error al crear el curso: " + e.getMessage() + "\"}");
         }
    }
    
    private void updateCourse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             Course updatedCourse = courseService.updateCourse(request);
             response.setContentType("application/json");
             Gson gson = new GsonBuilder()
                  .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                  .create();
             if (updatedCourse != null) {
                 String json = gson.toJson(updatedCourse);
                 response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
             } else {
                 response.getWriter().write("{\"success\": false, \"message\": \"Error al actualizar el curso\"}");
             }
         } catch (Exception e) {
             e.printStackTrace();
             response.setContentType("application/json");
             response.getWriter().write("{\"success\": false, \"message\": \"Error al actualizar el curso: " + e.getMessage() + "\"}");
         }
    }
}
