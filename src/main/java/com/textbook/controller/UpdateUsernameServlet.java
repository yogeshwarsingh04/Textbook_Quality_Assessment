package com.textbook.controller;

import com.textbook.dao.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/updateUsername")
public class UpdateUsernameServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String newUsername = request.getParameter("newUsername");
        UserDAO userDAO = new UserDAO();

        // 1. Check if username is empty
        if (newUsername == null || newUsername.trim().isEmpty()) {
            session.setAttribute("profileMessage", "Error: New username cannot be empty.");
            response.sendRedirect("profile.jsp");
            return;
        }

        // 2. Check if username is taken
        if (userDAO.isUsernameTaken(newUsername, userId)) {
            session.setAttribute("profileMessage", "Error: That username is already taken.");
            response.sendRedirect("profile.jsp");
            return;
        }

        // 3. Update the username
        if (userDAO.updateUsername(userId, newUsername)) {
            // IMPORTANT: Update the username in the session!
            session.setAttribute("username", newUsername);
            session.setAttribute("profileMessage", "Username updated successfully!");
        } else {
            session.setAttribute("profileMessage", "Error: Could not update username.");
        }
        
        response.sendRedirect("profile.jsp");
    }
}