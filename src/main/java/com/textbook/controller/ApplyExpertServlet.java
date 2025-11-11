package com.textbook.controller;

import com.textbook.util.DatabaseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/applyExpert")
public class ApplyExpertServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // 1. Get the logged-in user's ID from the session
        HttpSession session = request.getSession(false);
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 2. Get the form parameters
        String fullName = request.getParameter("full_name");
        String specialization = request.getParameter("specialization");
        String credentials = request.getParameter("credentials");

        // 3. Save to the Experts table
        // The 'status' column will default to 'pending' as we defined in the database
        String sql = "INSERT INTO Experts (expert_id, full_name, specialization, credentials) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // The expert_id is the same as the user_id
            ps.setInt(1, userId);
            ps.setString(2, fullName);
            ps.setString(3, specialization);
            ps.setString(4, credentials);

            ps.executeUpdate();

            // 4. Redirect to the home page after success
            response.sendRedirect("index.jsp");

        } catch (SQLException e) {
            // This error (1062) means "Duplicate entry"
            // It will happen if the user tries to apply a second time.
            if (e.getErrorCode() == 1062) {
                // Send a simple error message back
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<html><body>");
                out.println("<h3>You have already submitted an application.</h3>");
                out.println("<a href='index.jsp'>Back to Home</a>");
                out.println("</body></html>");
            } else {
                throw new ServletException("Database error during expert application", e);
            }
        }
    }
}