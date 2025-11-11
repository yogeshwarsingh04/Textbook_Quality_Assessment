package com.textbook.dao;

import com.textbook.model.ReadingList;
import com.textbook.model.Textbook; // We need this
import com.textbook.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReadingListDAO {

    /**
     * Creates a new reading list for a user.
     */
    public boolean createList(ReadingList list) {
        String sql = "INSERT INTO ReadingLists (user_id, title, description) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, list.getUserId());
            ps.setString(2, list.getTitle());
            ps.setString(3, list.getDescription());
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Adds a book to a reading list.
     */
    public boolean addBookToList(int listId, int bookId) {
        // "IGNORE" prevents an error if the book is already in the list
        String sql = "INSERT IGNORE INTO ReadingListItems (list_id, book_id) VALUES (?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, listId);
            ps.setInt(2, bookId);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Deletes a reading list (and all its items, via CASCADE)
     * owned by a specific user (for security).
     */
    public boolean deleteList(int listId, int userId) {
        String sql = "DELETE FROM ReadingLists WHERE list_id = ? AND user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, listId);
            ps.setInt(2, userId); // Ensures a user can only delete their own lists
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Gets all reading lists created by a specific user.
     */
    public List<ReadingList> getListsByUserId(int userId) {
        List<ReadingList> lists = new ArrayList<>();
        String sql = "SELECT * FROM ReadingLists WHERE user_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReadingList list = new ReadingList();
                    list.setListId(rs.getInt("list_id"));
                    list.setUserId(rs.getInt("user_id"));
                    list.setTitle(rs.getString("title"));
                    list.setDescription(rs.getString("description"));
                    list.setCreatedAt(rs.getTimestamp("created_at"));
                    lists.add(list);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lists;
    }
    
    /**
     * Gets all the *books* from a single reading list.
     */
    public List<Textbook> getBooksByListId(int listId) {
        List<Textbook> books = new ArrayList<>();
        // Join with the Textbooks table to get book details
        String sql = "SELECT t.* FROM Textbooks t " +
                     "JOIN ReadingListItems rli ON t.book_id = rli.book_id " +
                     "WHERE rli.list_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, listId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Re-use our existing Textbook model
                    Textbook book = new Textbook();
                    book.setBookId(rs.getInt("book_id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setSubject(rs.getString("subject"));
                    
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
    
    /**
     * Removes a book from a specific reading list.
     */
    public boolean removeBookFromList(int listId, int bookId) {
        String sql = "DELETE FROM ReadingListItems WHERE list_id = ? AND book_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, listId);
            ps.setInt(2, bookId);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}