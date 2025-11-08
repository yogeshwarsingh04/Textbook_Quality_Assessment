package com.textbook.controller;

import com.textbook.dao.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/updateEmail")
public class UpdateEmailServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String newEmail = request.getParameter("newEmail");
        UserDAO userDAO = new UserDAO();

        // 1. Check if email is empty
        if (newEmail == null || newEmail.trim().isEmpty()) {
            session.setAttribute("profileMessage", "Error: New email cannot be empty.");
            response.sendRedirect("profile.jsp");
            return;
        }
        
        // 2. Check if email is taken
        if (userDAO.isEmailTaken(newEmail, userId)) {
            session.setAttribute("profileMessage", "Error: That email is already in use.");
            response.sendRedirect("profile.jsp");
            return;
        }

        // 3. Update the email
        if (userDAO.updateEmail(userId, newEmail)) {
            session.setAttribute("profileMessage", "Email updated successfully!");
        } else {
            session.setAttribute("profileMessage", "Error: Could not update email.");
        }
        
        response.sendRedirect("profile.jsp");
    }
}