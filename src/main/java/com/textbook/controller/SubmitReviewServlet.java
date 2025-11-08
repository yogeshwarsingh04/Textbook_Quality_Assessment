package com.textbook.controller;

import com.textbook.dao.ReviewDAO;
import com.textbook.model.Review;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/submitReview")
public class SubmitReviewServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // 1. Get the logged-in user's ID from the session
        HttpSession session = request.getSession(false);
        Integer userId = (Integer) session.getAttribute("userId");

        // If user is not logged in, send them to login
        if (userId == null) {
            response.sendRedirect("login.jsp");
            return; 
        }

        try {
            // 2. Get all the form parameters
            int bookId = Integer.parseInt(request.getParameter("book_id"));
            String reviewTitle = request.getParameter("review_title");
            String reviewBody = request.getParameter("review_body");
            
            int ratingAccuracy = Integer.parseInt(request.getParameter("rating_accuracy"));
            int ratingClarity = Integer.parseInt(request.getParameter("rating_clarity"));
            int ratingEngagement = Integer.parseInt(request.getParameter("rating_engagement"));
            int ratingSensitivity = Integer.parseInt(request.getParameter("rating_sensitivity"));

            // 3. Create a new Review object and populate it
            Review review = new Review();
            review.setBookId(bookId);
            review.setUserId(userId);
            review.setReviewTitle(reviewTitle);
            review.setReviewBody(reviewBody);
            review.setRatingAccuracy(ratingAccuracy);
            review.setRatingClarity(ratingClarity);
            review.setRatingEngagement(ratingEngagement);
            review.setRatingSensitivity(ratingSensitivity);

            // 4. Save the review to the database
            ReviewDAO reviewDAO = new ReviewDAO();
            boolean success = reviewDAO.addReview(review);
            
            // 5. Redirect back to the book details page
            // This is crucial. The user sees their review immediately.
            response.sendRedirect("bookDetails.jsp?id=" + bookId);

        } catch (NumberFormatException e) {
            // Handle error if someone messes with the form data (e.g., non-number rating)
            e.printStackTrace();
            response.sendRedirect("index.jsp"); // Send to home on error
        }
    }
}