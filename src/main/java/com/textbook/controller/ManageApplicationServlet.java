package com.textbook.controller;

import com.textbook.dao.ExpertDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/manageApplication")
public class ManageApplicationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // 1. Security Check: Ensure user is an admin
        HttpSession session = request.getSession(false);
        String role = (session != null) ? (String) session.getAttribute("role") : null;

        if (role == null || !role.equals("admin")) {
            // If not an admin, boot them to the home page
            response.sendRedirect("index.jsp");
            return;
        }

        try {
            // 2. Get the parameters from the admin.jsp form
            int userId = Integer.parseInt(request.getParameter("userId"));
            String action = request.getParameter("action"); // "approved" or "rejected"

            // 3. Call the DAO to update the database
            ExpertDAO expertDAO = new ExpertDAO();
            boolean success = false;
            
            if ("approved".equals(action) || "rejected".equals(action)) {
                success = expertDAO.updateApplicationStatus(userId, action);
            }

            if (!success) {
                // Handle a failed database update
                // You could set an error message in the session here
            	System.out.println("Database could not be updated!!");
            }

            // 4. Redirect back to the admin panel
            response.sendRedirect("admin.jsp");

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("admin.jsp");
        }
    }
}