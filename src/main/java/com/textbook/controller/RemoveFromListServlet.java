package com.textbook.controller;

import com.textbook.dao.ReadingListDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/removeFromList")
public class RemoveFromListServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // 1. Security Check: Get user ID and role
        Integer userId = (Integer) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        // Only educators or admins can remove from lists
        if (userId == null || (!role.equals("educator") && !role.equals("admin"))) {
            response.sendRedirect("index.jsp");
            return;
        }

        try {
            // 2. Get form parameters
            int listId = Integer.parseInt(request.getParameter("list_id"));
            int bookId = Integer.parseInt(request.getParameter("book_id"));

            // 3. Call the DAO to remove the book
            ReadingListDAO listDAO = new ReadingListDAO();
            listDAO.removeBookFromList(listId, bookId);
            listDAO.removeBookFromList(listId, bookId);

            response.sendRedirect("viewList.jsp?id=" + listId);
            
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("myLists.jsp"); // Redirect to all lists on error
        }
    }
}