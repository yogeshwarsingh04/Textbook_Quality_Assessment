package com.textbook.controller;

import com.textbook.dao.TextbookDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/manageBook")
public class ManageBookServlet extends HttpServlet {

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // 1. Security Check: Ensure user is an admin
        HttpSession session = request.getSession(false);
        String role = (session != null) ? (String) session.getAttribute("role") : null;

        if (role == null || !role.equals("admin")) {
            response.sendRedirect("index.jsp");
            return;
        }

        try {
            // 2. Get the parameters from the admin.jsp form
            int bookId = Integer.parseInt(request.getParameter("bookId"));
            String action = request.getParameter("action"); // "approved" or "rejected"

            TextbookDAO textbookDAO = new TextbookDAO();
            
            if ("approved".equals(action)) {
                // If approved, update the status
                textbookDAO.updateTextbookStatus(bookId, "approved");
                
            } else if ("rejected".equals(action)) {
                // If rejected, delete the book
                textbookDAO.deleteTextbook(bookId);
            }
            // 4. Redirect back to the admin panel
            response.sendRedirect("admin.jsp");

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("admin.jsp");
        }
    }
}