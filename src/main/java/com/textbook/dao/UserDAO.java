package com.textbook.dao;

import com.textbook.model.User;
import com.textbook.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    /**
     * Fetches a single user's details by their ID.
     */
    public User getUserById(int userId) {
        User user = null;
        String sql = "SELECT * FROM Users WHERE user_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setRole(rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    
    /**
     * Finds a user by their email address.
     */
    public User getUserByEmail(String email) {
        User user = null;
        String sql = "SELECT * FROM Users WHERE email = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setRole(rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    
    /**
     * Saves a password reset token and its expiry time for a user.
     */
    public boolean setResetToken(int userId, String token, java.sql.Timestamp expiry) {
        String sql = "UPDATE Users SET reset_token = ?, token_expiry = ? WHERE user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, token);
            ps.setTimestamp(2, expiry);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Finds a user by their reset token, but only if it's not expired.
     */
    public User getUserByResetToken(String token) {
        User user = null;
        // Check that the token matches AND that the expiry time is in the future
        String sql = "SELECT * FROM Users WHERE reset_token = ? AND token_expiry > NOW()";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, token);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    // We don't need to load the password hash
                    user.setRole(rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    
    /**
     * Clears the reset token and expiry from a user's record.
     * This is called after a successful password reset.
     */
    public boolean clearResetToken(int userId) {
        String sql = "UPDATE Users SET reset_token = NULL, token_expiry = NULL WHERE user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if a username is already taken by ANOTHER user.
     */
    public boolean isUsernameTaken(String username, int currentUserId) {
        String sql = "SELECT 1 FROM Users WHERE username = ? AND user_id != ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setInt(2, currentUserId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // true if a record was found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // Fail safe
        }
    }

    /**
     * Updates a user's username.
     */
    public boolean updateUsername(int userId, String newUsername) {
        String sql = "UPDATE Users SET username = ? WHERE user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, newUsername);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates a user's password.
     */
    public boolean updatePassword(int userId, String newHashedPassword) {
        String sql = "UPDATE Users SET password_hash = ? WHERE user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, newHashedPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Checks if an email is already taken by ANOTHER user.
     */
    public boolean isEmailTaken(String email, int currentUserId) {
        String sql = "SELECT 1 FROM Users WHERE email = ? AND user_id != ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            ps.setInt(2, currentUserId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // true if a record was found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // Fail safe
        }
    }

    /**
     * Updates a user's email.
     */
    public boolean updateEmail(int userId, String newEmail) {
        String sql = "UPDATE Users SET email = ? WHERE user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, newEmail);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}