package com.textbook.controller;

import com.textbook.dao.UserDAO;
import com.textbook.model.User;
import com.textbook.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/updatePassword")
public class UpdatePasswordServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // 1. Check if new passwords match
        if (!newPassword.equals(confirmPassword)) {
            session.setAttribute("profileMessage", "Error: New passwords do not match.");
            response.sendRedirect("profile.jsp");
            return;
        }

        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserById(userId);

        // 2. Check if old password is correct
        if (PasswordUtil.checkPassword(oldPassword, user.getPasswordHash())) {
            
            // 3. Hash and update new password
            String newHashedPassword = PasswordUtil.hashPassword(newPassword);
            if (userDAO.updatePassword(userId, newHashedPassword)) {
                session.setAttribute("profileMessage", "Password updated successfully!");
            } else {
                session.setAttribute("profileMessage", "Error: Could not update password.");
            }
        } else {
            session.setAttribute("profileMessage", "Error: Incorrect old password.");
        }

        response.sendRedirect("profile.jsp");
    }
}