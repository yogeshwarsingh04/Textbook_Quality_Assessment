package com.textbook.controller;

import com.textbook.dao.ReadingListDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/deleteList")
public class DeleteListServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // 1. Security Check: Get user ID
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // 2. Get the list ID from the form
            int listId = Integer.parseInt(request.getParameter("list_id"));

            // 3. Call the DAO to delete the list
            ReadingListDAO listDAO = new ReadingListDAO();
            boolean success = listDAO.deleteList(listId, userId);

            // 4. Set a flash message
            if (success) {
                session.setAttribute("listMessage", "List successfully deleted.");
            } else {
                session.setAttribute("listMessage", "Error: Could not delete list.");
            }
            
            // 5. Redirect back to the lists page
            response.sendRedirect("myLists.jsp");
            
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("myLists.jsp");
        }
    }
}