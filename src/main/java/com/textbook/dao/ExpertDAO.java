package com.textbook.dao;

import com.textbook.model.Expert;
import com.textbook.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExpertDAO {

    /**
     * Fetches all pending applications, joining with the Users table
     * to get their username and email.
     */
    public List<Expert> getPendingApplications() {
        List<Expert> applications = new ArrayList<>();
        String sql = "SELECT e.*, u.username, u.email FROM Experts e " +
                     "JOIN Users u ON e.expert_id = u.user_id " +
                     "WHERE e.status = 'pending'";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Expert expert = new Expert();
                expert.setExpertId(rs.getInt("expert_id"));
                expert.setFullName(rs.getString("full_name"));
                expert.setSpecialization(rs.getString("specialization"));
                expert.setCredentials(rs.getString("credentials"));
                expert.setStatus(rs.getString("status"));
                expert.setUsername(rs.getString("username"));
                expert.setEmail(rs.getString("email"));
                applications.add(expert);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return applications;
    }

    /**
     * Updates an application status.
     * If status is "approved", it also updates the Users table.
     * This runs as a TRANSACTION to ensure data consistency.
     */
    public boolean updateApplicationStatus(int expertId, String newStatus) {
        String sqlUpdateExperts = "UPDATE Experts SET status = ? WHERE expert_id = ?";
        String sqlUpdateUsers = "UPDATE Users SET role = 'expert' WHERE user_id = ?";
        
        Connection conn = null;
        try {
            conn = DatabaseUtil.getConnection();
            // Start a transaction
            conn.setAutoCommit(false);

            // 1. Update the Experts table
            try (PreparedStatement psExperts = conn.prepareStatement(sqlUpdateExperts)) {
                psExperts.setString(1, newStatus);
                psExperts.setInt(2, expertId);
                psExperts.executeUpdate();
            }

            // 2. If approved, update the Users table
            if (newStatus.equals("approved")) {
                try (PreparedStatement psUsers = conn.prepareStatement(sqlUpdateUsers)) {
                    psUsers.setInt(1, expertId);
                    psUsers.executeUpdate();
                }
            }
            
            // If all queries succeed, commit the transaction
            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            // If any query fails, roll back the entire transaction
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            // Always set auto-commit back to true and close connection
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}