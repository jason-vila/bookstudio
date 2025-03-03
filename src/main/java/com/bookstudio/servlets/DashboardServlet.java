package com.bookstudio.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstudio.models.DashboardData;
import com.bookstudio.services.DashboardService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet("/DashboardServlet")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private DashboardService dashboardService = new DashboardService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         request.setCharacterEncoding("UTF-8");
         response.setCharacterEncoding("UTF-8");
         
         String type = request.getParameter("type");
         if (type == null || type.equals("getDashboardData")) {
             getDashboardData(request, response);
         } else {
             response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
         }
    }
    
    private void getDashboardData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             DashboardData dashboardData = dashboardService.getDashboardData();
             response.setContentType("application/json");
             
             Gson gson = new GsonBuilder().create();
             String json = gson.toJson(dashboardData);
             response.getWriter().write(json);
         } catch(Exception e) {
             e.printStackTrace();
             response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving dashboard data");
         }
    }
}
