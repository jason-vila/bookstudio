package com.bookstudio.servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstudio.models.Book;
import com.bookstudio.services.BookService;
import com.bookstudio.utils.LocalDateAdapter;
import com.bookstudio.utils.SelectOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet("/BookServlet")
public class BookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private BookService bookService = new BookService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         request.setCharacterEncoding("UTF-8");
         response.setCharacterEncoding("UTF-8");
         String type = request.getParameter("type");
         switch (type) {
             case "list":
                 listBooks(request, response);
                 break;
             case "details":
                 getBook(request, response);
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
                 createBook(request, response);
                 break;
             case "update":
                 updateBook(request, response);
                 break;
             default:
                 response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
                 break;
         }
    }
    
    private void listBooks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             List<Book> bookData = bookService.listBooks();
             response.setContentType("application/json");
             Gson gson = new GsonBuilder()
                 .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                 .create();
             String json = gson.toJson(bookData);
             response.getWriter().write(json);
         } catch(Exception e) {
             e.printStackTrace();
             response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al listar libros");
         }
    }
    
    private void getBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             String bookId = request.getParameter("bookId");
             Book book = bookService.getBook(bookId);
             response.setContentType("application/json");
             Gson gson = new GsonBuilder()
                 .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                 .create();
             String json = gson.toJson(book);
             response.getWriter().write(json);
         } catch(Exception e) {
             e.printStackTrace();
             response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener libro");
         }
    }
    
    private void createBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             Book createdBook = bookService.createBook(request);
             response.setContentType("application/json");
             Gson gson = new GsonBuilder()
                 .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                 .create();
             String json = gson.toJson(createdBook);
             response.getWriter().write(json);
         } catch(Exception e) {
             e.printStackTrace();
             response.setContentType("application/json");
             response.getWriter().write("{\"success\": false, \"message\": \"Error al crear el libro: " + e.getMessage() + "\"}");
         }
    }
    
    private void updateBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             Book updatedBook = bookService.updateBook(request);
             response.setContentType("application/json");
             Gson gson = new GsonBuilder()
                 .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                 .create();
             if (updatedBook != null) {
                 String json = gson.toJson(updatedBook);
                 response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
             } else {
                 response.getWriter().write("{\"success\": false, \"message\": \"Error al actualizar el libro\"}");
             }
         } catch(Exception e) {
             e.printStackTrace();
             response.setContentType("application/json");
             response.getWriter().write("{\"success\": false, \"message\": \"Error al actualizar el libro: " + e.getMessage() + "\"}");
         }
    }
    
    private void populateSelects(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             SelectOptions selectOptions = bookService.populateSelects();
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
