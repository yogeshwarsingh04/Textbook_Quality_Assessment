package com.textbook.controller;

import com.textbook.util.DatabaseUtil;
import com.textbook.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession; // Make sure this is imported
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String plainPassword = request.getParameter("password");
        
        // --- NEW: Get the session first to set messages ---
        HttpSession session = request.getSession();

        String sql = "SELECT user_id, password_hash, role FROM Users WHERE username = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                
                // 1. Check if user exists
                if (rs.next()) {
                    String hashedPassword = rs.getString("password_hash");
                    
                    // 2. Check if password matches
                    if (PasswordUtil.checkPassword(plainPassword, hashedPassword)) {
                        
                        // --- SUCCESS! ---
                        // Clear any old error messages
                        session.removeAttribute("loginMessage");
                        
                        // Store user info in the session
                        session.setAttribute("userId", rs.getInt("user_id"));
                        session.setAttribute("username", username);
                        session.setAttribute("role", rs.getString("role"));
                        
                        // Redirect to the home page
                        response.sendRedirect("index.jsp");
                        
                    } else {
                        // Password was incorrect ---
                        session.setAttribute("loginMessage", "Error: Incorrect password.");
                        response.sendRedirect("login.jsp");
                    }
                    
                } else {
                    // User was not found ---
                    session.setAttribute("loginMessage", "Error: User not found.");
                    response.sendRedirect("login.jsp");
                }
            }

        } catch (SQLException e) {
            throw new ServletException("Database error during login", e);
        }
    }
}