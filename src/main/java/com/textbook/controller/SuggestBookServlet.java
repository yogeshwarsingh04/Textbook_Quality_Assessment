package com.textbook.controller;

import com.textbook.dao.TextbookDAO;
import com.textbook.model.Textbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/suggestBook")
public class SuggestBookServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // 1. Security Check: Ensure user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // 2. Get all the form parameters
            String title = request.getParameter("title");
            String author = request.getParameter("author");
            String isbn = request.getParameter("isbn");
            String publisher = request.getParameter("publisher");
            String subject = request.getParameter("subject");
            String gradeLevel = request.getParameter("grade_level");
            
            // Handle number format for year
            int publicationYear = 0; // Default
            String yearParam = request.getParameter("publication_year");
            if (yearParam != null && !yearParam.isEmpty()) {
                publicationYear = Integer.parseInt(yearParam);
            }

            // 3. Create a new Textbook object
            Textbook book = new Textbook();
            book.setTitle(title);
            book.setAuthor(author);
            book.setIsbn(isbn);
            book.setPublisher(publisher);
            book.setSubject(subject);
            book.setGradeLevel(gradeLevel);
            book.setPublicationYear(publicationYear);

            // 4. Save the new book suggestion (its status will be 'pending')
            TextbookDAO textbookDAO = new TextbookDAO();
            boolean success = textbookDAO.suggestTextbook(book);

            // 5. Set a success message and redirect
            if (success) {
                session.setAttribute("suggestMessage", "Thank you! Your suggestion has been sent for review.");
            } else {
                session.setAttribute("suggestMessage", "Error: Could not submit your suggestion.");
            }
            response.sendRedirect("suggestBook.jsp");

        } catch (NumberFormatException e) {
            // Handle error if the year is not a valid number
            e.printStackTrace();
            session.setAttribute("suggestMessage", "Error: Please enter a valid year.");
            response.sendRedirect("suggestBook.jsp");
        }
    }
}