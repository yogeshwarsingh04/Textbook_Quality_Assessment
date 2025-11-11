package com.textbook.controller;

import com.textbook.util.DatabaseUtil;
import com.textbook.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String plainPassword = request.getParameter("password");
        String role = request.getParameter("role");

        String hashedPassword = PasswordUtil.hashPassword(plainPassword);

        String sql = "INSERT INTO Users (username, email, password_hash, role) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, hashedPassword);
            ps.setString(4, role);
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                // Success! Redirect to the login page (which we'll create next)
                response.sendRedirect("login.jsp");
            } else {
                response.getWriter().println("Registration failed. Please try again.");
            }

        } catch (SQLException e) {
            // Handle database errors (e.g., duplicate username/email)
            if (e.getErrorCode() == 1062) { // MySQL code for "Duplicate entry"
                response.getWriter().println("Error: Username or Email already exists.");
            } else {
                throw new ServletException("Database error during registration", e);
            }
        }
    }
}