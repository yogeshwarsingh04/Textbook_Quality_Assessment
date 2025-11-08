package com.textbook.controller;

import com.textbook.dao.UserDAO;
import com.textbook.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.UUID;

@WebServlet("/forgotPassword")
public class ForgotPasswordServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByEmail(email);
        
        HttpSession session = request.getSession();

        if (user == null) {
            // User not found, set an error message
            session.setAttribute("resetMessage", "Error: No account found with that email address.");
            session.setAttribute("messageClass", "error");
            response.sendRedirect("forgotPassword.jsp");
            return;
        }

        // --- User was found, proceed with token generation ---

        // 1. Generate a secure, unique token
        String token = UUID.randomUUID().toString();

        // 2. Set an expiration time (e.g., 1 hour from now)
        long currentTimeMillis = System.currentTimeMillis();
        long expiryTimeMillis = currentTimeMillis + (60 * 60 * 1000); // 1 hour
        Timestamp expiryTimestamp = new Timestamp(expiryTimeMillis);

        // 3. Save the token and expiry to the database
        boolean success = userDAO.setResetToken(user.getUserId(), token, expiryTimestamp);

        if (success) {
            // 4. SIMULATE SENDING THE EMAIL
            
            String resetLink = request.getScheme() + "://" +
                               request.getServerName() + ":" +
                               request.getServerPort() +
                               request.getContextPath() +
                               "/resetPassword.jsp?token=" + token;

            // Set a success message for the user
            String successMessage = "<b>Success!</b> A password reset link has been generated." +
                                    "<br><br><b>(This is a simulation, no email was sent.)</b>" +
                                    "<br><br>Click this link to reset your password:" +
                                    "<br><a href='" + resetLink + "'>Reset My Password</a>";
            
            session.setAttribute("resetMessage", successMessage);
            session.setAttribute("messageClass", "success");
            
        } else {
            // Database error
            session.setAttribute("resetMessage", "Error: Could not process your request. Please try again.");
            session.setAttribute("messageClass", "error");
        }
        
        response.sendRedirect("forgotPassword.jsp");
    }
}