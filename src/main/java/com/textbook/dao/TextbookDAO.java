package com.textbook.dao;

import com.textbook.model.Textbook;
import com.textbook.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TextbookDAO {

	public List<Textbook> getAllTextbooks() {
        List<Textbook> textbooks = new ArrayList<>();
        
        // Join with Reviews, calculate average, and group by book
        String sql = "SELECT t.*, AVG((r.rating_accuracy + r.rating_clarity + r.rating_engagement + r.rating_sensitivity) / 4.0) as avg_rating " +
                     "FROM Textbooks t " +
                     "LEFT JOIN Reviews r ON t.book_id = r.book_id " +
                     "WHERE t.status = 'approved' " +
                     "GROUP BY t.book_id";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Textbook book = new Textbook();
                book.setBookId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setIsbn(rs.getString("isbn"));
                book.setPublisher(rs.getString("publisher"));
                book.setSubject(rs.getString("subject"));
                book.setGradeLevel(rs.getString("grade_level"));
                book.setPublicationYear(rs.getInt("publication_year"));
                
                // Get the calculated average rating
                // rs.getDouble() returns 0.0 if the value is NULL (e.g., no reviews)
                book.setAverageRating(rs.getDouble("avg_rating"));
                
                textbooks.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return textbooks;
    }
    
    public Textbook getTextbookById(int bookId) {
        Textbook book = null;
        String sql = "SELECT * FROM Textbooks WHERE book_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set the ID parameter in the query
            ps.setInt(1, bookId);

            try (ResultSet rs = ps.executeQuery()) {
                // Check if a result was returned
                if (rs.next()) {
                    // A book was found, populate the object
                    book = new Textbook();
                    book.setBookId(rs.getInt("book_id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setIsbn(rs.getString("isbn"));
                    book.setPublisher(rs.getString("publisher"));
                    book.setSubject(rs.getString("subject"));
                    book.setGradeLevel(rs.getString("grade_level"));
                    book.setPublicationYear(rs.getInt("publication_year"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions
        }
        
        return book; // Return the populated book, or null if not found
    }
    
    public List<Textbook> searchTextbooks(String query) {
        List<Textbook> textbooks = new ArrayList<>();
        
        // Join, calculate average, add WHERE, and group
        String sql = "SELECT t.*, AVG((r.rating_accuracy + r.rating_clarity + r.rating_engagement + r.rating_sensitivity) / 4.0) as avg_rating " +
                     "FROM Textbooks t " +
                     "LEFT JOIN Reviews r ON t.book_id = r.book_id " +
                     "WHERE (t.title LIKE ? OR t.author LIKE ? OR t.subject LIKE ?) AND t.status = 'approved' " +
                     "GROUP BY t.book_id";
        
        String searchQuery = "%" + query + "%";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, searchQuery);
            ps.setString(2, searchQuery);
            ps.setString(3, searchQuery);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Textbook book = new Textbook();
                    book.setBookId(rs.getInt("book_id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setIsbn(rs.getString("isbn"));
                    book.setPublisher(rs.getString("publisher"));
                    book.setSubject(rs.getString("subject"));
                    book.setGradeLevel(rs.getString("grade_level"));
                    book.setPublicationYear(rs.getInt("publication_year"));
                    
                    // Get the calculated average rating
                    book.setAverageRating(rs.getDouble("avg_rating"));
                    
                    textbooks.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return textbooks;
    }
    
    /**
     * Gets a list of book titles for search suggestions.
     * @param query The user's typing.
     * @return A list of matching titles.
     */
    public List<String> getBookTitleSuggestions(String query) {
        List<String> suggestions = new ArrayList<>();
        
        // Find titles starting with the query, limited to 10 results
        String sql = "SELECT title FROM Textbooks WHERE status = 'approved' AND title LIKE ? LIMIT 10";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // The '%' wildcard means "anything can come after"
        	ps.setString(1, "%" + query + "%");
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    suggestions.add(rs.getString("title"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suggestions;
    }
    
    /**
     * Saves a new book suggestion to the database.
     * The 'status' column will automatically default to 'pending'.
     */
    public boolean suggestTextbook(Textbook book) {
        String sql = "INSERT INTO Textbooks (title, isbn, author, publisher, subject, grade_level, publication_year) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getIsbn());
            ps.setString(3, book.getAuthor());
            ps.setString(4, book.getPublisher());
            ps.setString(5, book.getSubject());
            ps.setString(6, book.getGradeLevel());
            ps.setInt(7, book.getPublicationYear());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Fetches all textbooks with a 'pending' status for the admin panel.
     */
    public List<Textbook> getPendingTextbooks() {
        List<Textbook> textbooks = new ArrayList<>();
        String sql = "SELECT * FROM Textbooks WHERE status = 'pending'";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Textbook book = new Textbook();
                book.setBookId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setIsbn(rs.getString("isbn"));
                book.setSubject(rs.getString("subject"));
                
                textbooks.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return textbooks;
    }

    /**
     * Updates a book's status (approved, rejected).
     */
    public boolean updateTextbookStatus(int bookId, String newStatus) {
        String sql = "UPDATE Textbooks SET status = ? WHERE book_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, newStatus);
            ps.setInt(2, bookId);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Permanently deletes a textbook from the database.
     * @param bookId The ID of the book to delete.
     * @return true if successful, false otherwise.
     */
    public boolean deleteTextbook(int bookId) {
        String sql = "DELETE FROM Textbooks WHERE book_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, bookId);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}