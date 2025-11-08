package com.textbook.dao;

import com.textbook.model.Review;
import com.textbook.model.RatingSummary;
import com.textbook.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    /**
     * Saves a new review to the database.
     * @param review The Review object to save.
     * @return true if successful, false otherwise.
     */
    public boolean addReview(Review review) {
        String sql = "INSERT INTO Reviews (book_id, user_id, review_title, review_body, " +
                     "rating_accuracy, rating_clarity, rating_engagement, rating_sensitivity) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, review.getBookId());
            ps.setInt(2, review.getUserId());
            ps.setString(3, review.getReviewTitle());
            ps.setString(4, review.getReviewBody());
            ps.setInt(5, review.getRatingAccuracy());
            ps.setInt(6, review.getRatingClarity());
            ps.setInt(7, review.getRatingEngagement());
            ps.setInt(8, review.getRatingSensitivity());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Fetches all reviews for a specific book, including the reviewer's username.
     * @param bookId The ID of the book.
     * @return A list of Review objects.
     */
    public List<Review> getReviewsByBookId(int bookId) {
        List<Review> reviews = new ArrayList<>();
        
        // We now also SELECT u.role from the Users table
        String sql = "SELECT r.*, u.username, u.role FROM Reviews r " +
                     "JOIN Users u ON r.user_id = u.user_id " +
                     "WHERE r.book_id = ? " +
                     "ORDER BY r.created_at DESC";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, bookId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Review review = new Review();
                    review.setReviewId(rs.getInt("review_id"));
                    review.setBookId(rs.getInt("book_id"));
                    review.setUserId(rs.getInt("user_id"));
                    review.setReviewTitle(rs.getString("review_title"));
                    review.setReviewBody(rs.getString("review_body"));
                    review.setCreatedAt(rs.getTimestamp("created_at"));
                    
                    review.setRatingAccuracy(rs.getInt("rating_accuracy"));
                    review.setRatingClarity(rs.getInt("rating_clarity"));
                    review.setRatingEngagement(rs.getInt("rating_engagement"));
                    review.setRatingSensitivity(rs.getInt("rating_sensitivity"));
                    
                    review.setUsername(rs.getString("username"));
                    
                    // Populate the new role field
                    review.setRole(rs.getString("role"));
                    
                    reviews.add(review);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reviews;
    }
    
    public RatingSummary getRatingSummary(int bookId) {
        RatingSummary summary = new RatingSummary();
        
        // This query calculates all averages and the total count in one go
        String sql = "SELECT " +
                     "  COUNT(*) as reviewCount, " +
                     "  AVG((rating_accuracy + rating_clarity + rating_engagement + rating_sensitivity) / 4.0) as overallAvg, " +
                     "  AVG(rating_accuracy) as avgAccuracy, " +
                     "  AVG(rating_clarity) as avgClarity, " +
                     "  AVG(rating_engagement) as avgEngagement, " +
                     "  AVG(rating_sensitivity) as avgSensitivity " +
                     "FROM Reviews " +
                     "WHERE book_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, bookId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    summary.setReviewCount(rs.getInt("reviewCount"));
                    
                    // Only set averages if there are reviews
                    if (summary.getReviewCount() > 0) {
                        summary.setOverallAverage(rs.getDouble("overallAvg"));
                        summary.setAverageAccuracy(rs.getDouble("avgAccuracy"));
                        summary.setAverageClarity(rs.getDouble("avgClarity"));
                        summary.setAverageEngagement(rs.getDouble("avgEngagement"));
                        summary.setAverageSensitivity(rs.getDouble("avgSensitivity"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return summary;
    }
}