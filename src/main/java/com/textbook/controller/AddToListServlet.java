package com.textbook.controller;

import com.textbook.dao.ReadingListDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/addToList")
public class AddToListServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // 1. Security Check: Get user ID and role
        Integer userId = (Integer) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        // Only educators or admins can add to lists
        if (userId == null || (!role.equals("educator") && !role.equals("admin"))) {
            response.sendRedirect("index.jsp");
            return;
        }

        try {
            // 2. Get form parameters
            int listId = Integer.parseInt(request.getParameter("list_id"));
            int bookId = Integer.parseInt(request.getParameter("book_id"));
            String returnUrl = request.getParameter("return_url");

            // 3. Save to database
            ReadingListDAO listDAO = new ReadingListDAO();
            boolean success = listDAO.addBookToList(listId, bookId);

            if (success) {
            	session.setAttribute("bookDetailMessage", "Book successfully added to your list!");
                session.setAttribute("messageClass", "success");
            } else {
                // This handles the case where the book is already in the list
                session.setAttribute("bookDetailMessage", "This book is already in that list.");
                session.setAttribute("messageClass", "error"); // Use an error style
            }
            
            // 4. Redirect back to the page the user was on
            if (returnUrl != null && !returnUrl.isEmpty()) {
                response.sendRedirect(returnUrl);
            } else {
                response.sendRedirect("index.jsp");
            }
            
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("index.jsp");
        }
    }
}