package com.bookstudio.servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstudio.models.Loan;
import com.bookstudio.services.LoanService;
import com.bookstudio.utils.LocalDateAdapter;
import com.bookstudio.utils.SelectOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet("/LoanServlet")
public class LoanServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private LoanService loanService = new LoanService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         request.setCharacterEncoding("UTF-8");
         response.setCharacterEncoding("UTF-8");
         String type = request.getParameter("type");
         switch(type) {
             case "list":
                 listLoans(request, response);
                 break;
             case "details":
                 getLoan(request, response);
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
                 createLoan(request, response);
                 break;
             case "update":
                 updateLoan(request, response);
                 break;
             case "confirmReturn":
            	 confirmReturn(request, response);
                 break;
             default:
                 response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
                 break;
         }
    }
    
    private void listLoans(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             List<Loan> loanData = loanService.listLoans();
             response.setContentType("application/json");
             Gson gson = new GsonBuilder()
                 .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                 .create();
             String json = gson.toJson(loanData);
             response.getWriter().write(json);
         } catch(Exception e) {
             e.printStackTrace();
             response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al listar préstamos");
         }
    }
    
    private void getLoan(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             String loanId = request.getParameter("loanId");
             Loan loan = loanService.getLoan(loanId);
             response.setContentType("application/json");
             Gson gson = new GsonBuilder()
                 .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                 .create();
             String json = gson.toJson(loan);
             response.getWriter().write(json);
         } catch(Exception e) {
             e.printStackTrace();
             response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener préstamo");
         }
    }
    
    private void createLoan(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             Loan createdLoan = loanService.createLoan(request);
             response.setContentType("application/json");
             Gson gson = new GsonBuilder()
                 .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                 .create();
             String json = gson.toJson(createdLoan);
             response.getWriter().write(json);
         } catch(Exception e) {
             e.printStackTrace();
             response.setContentType("application/json");
             response.getWriter().write("{\"success\": false, \"message\": \"Error al crear el préstamo: " + e.getMessage() + "\"}");
         }
    }
    
    private void updateLoan(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             Loan updatedLoan = loanService.updateLoan(request);
             response.setContentType("application/json");
             Gson gson = new GsonBuilder()
                 .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                 .create();
             if (updatedLoan != null) {
                 String json = gson.toJson(updatedLoan);
                 response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
             } else {
                 response.getWriter().write("{\"success\": false, \"message\": \"Error al actualizar el préstamo\"}");
             }
         } catch(Exception e) {
             e.printStackTrace();
             response.setContentType("application/json");
             response.getWriter().write("{\"success\": false, \"message\": \"Error al actualizar el préstamo: " + e.getMessage() + "\"}");
         }
    }
    
    private void confirmReturn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             String loanId = request.getParameter("loanId");
             String newStatus = request.getParameter("newStatus").toLowerCase();
             
             Loan loan = loanService.getLoan(loanId);
             String currentStatus = loan.getStatus();
             if ("devuelto".equals(currentStatus)) {
                 response.setContentType("application/json");
                 response.getWriter().write("{\"success\": false, \"message\": \"El préstamo ya ha sido devuelto.\"}");
                 return;
             }
             
             int result = loanService.confirmReturn(loanId, newStatus);
             response.setContentType("application/json");
             if (result > 0) {
                 response.getWriter().write("{\"success\": true, \"loanId\": " + loanId + ", \"newStatus\": \"" + newStatus + "\"}");
             } else {
                 response.getWriter().write("{\"success\": false, \"message\": \"Error al cambiar el estado\"}");
             }
         } catch(Exception e) {
             e.printStackTrace();
             response.setContentType("application/json");
             response.getWriter().write("{\"success\": false, \"message\": \"Error al cambiar el estado: " + e.getMessage() + "\"}");
         }
    }
    
    private void populateSelects(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             SelectOptions selectOptions = loanService.populateSelects();
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
