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

@WebServlet("/resetPassword")
public class ResetPasswordServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String token = request.getParameter("token");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // 1. Check if passwords match
        if (!newPassword.equals(confirmPassword)) {
            session.setAttribute("resetMessage", "Error: New passwords do not match.");
            session.setAttribute("messageClass", "error");
            response.sendRedirect("resetPassword.jsp?token=" + token);
            return;
        }

        UserDAO userDAO = new UserDAO();
        // 2. Validate the token (checks for expiry too)
        User user = userDAO.getUserByResetToken(token);

        if (user == null) {
            // Token is invalid or expired
            session.setAttribute("resetMessage", "Error: Invalid or expired token. Please try again.");
            session.setAttribute("messageClass", "error");
            response.sendRedirect("resetPassword.jsp?token=" + token);
            return;
        }

        // --- Token and passwords are valid ---
        
        // 3. Hash the new password
        String newHashedPassword = PasswordUtil.hashPassword(newPassword);

        // 4. Update the password in the database
        boolean success = userDAO.updatePassword(user.getUserId(), newHashedPassword);

        if (success) {
            // 5. Clear the reset token for security
            userDAO.clearResetToken(user.getUserId());

            // 6. Send user to login page with a success message
            session.setAttribute("loginMessage", "Success! Your password has been reset. Please log in.");
            response.sendRedirect("login.jsp");
        } else {
            // Database error
            session.setAttribute("resetMessage", "Error: Could not update your password. Please try again.");
            session.setAttribute("messageClass", "error");
            response.sendRedirect("resetPassword.jsp?token=" + token);
        }
    }
}