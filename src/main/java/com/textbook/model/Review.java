package com.textbook.model;

import java.sql.Timestamp;

public class Review {

    private int reviewId;
    private int bookId;
    private int userId;
    private String reviewTitle;
    private String reviewBody;
    private Timestamp createdAt;
    
    // Quality Metrics
    private int ratingAccuracy;
    private int ratingClarity;
    private int ratingEngagement;
    private int ratingSensitivity;
    
    // Extra field to hold the username of the reviewer
    private String username;
    // Extra field to hold the role of the reviewer
    private String role;

    // (You can auto-generate these in Eclipse: Right-click > Source > Generate Getters and Setters...)

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    public String getReviewBody() {
        return reviewBody;
    }

    public void setReviewBody(String reviewBody) {
        this.reviewBody = reviewBody;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getRatingAccuracy() {
        return ratingAccuracy;
    }

    public void setRatingAccuracy(int ratingAccuracy) {
        this.ratingAccuracy = ratingAccuracy;
    }

    public int getRatingClarity() {
        return ratingClarity;
    }

    public void setRatingClarity(int ratingClarity) {
        this.ratingClarity = ratingClarity;
    }

    public int getRatingEngagement() {
        return ratingEngagement;
    }

    public void setRatingEngagement(int ratingEngagement) {
        this.ratingEngagement = ratingEngagement;
    }

    public int getRatingSensitivity() {
        return ratingSensitivity;
    }

    public void setRatingSensitivity(int ratingSensitivity) {
        this.ratingSensitivity = ratingSensitivity;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}