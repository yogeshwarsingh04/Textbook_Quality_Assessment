package com.textbook.controller;

import com.textbook.dao.ReadingListDAO;
import com.textbook.model.ReadingList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/createList")
public class CreateListServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // 1. Security Check: Get user ID and role
        Integer userId = (Integer) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        // Only educators or admins can create lists
        if (userId == null || (!role.equals("educator") && !role.equals("admin"))) {
            response.sendRedirect("index.jsp");
            return;
        }

        // 2. Get form parameters
        String title = request.getParameter("title");
        String description = request.getParameter("description");

        // 3. Create and populate the ReadingList object
        ReadingList list = new ReadingList();
        list.setUserId(userId);
        list.setTitle(title);
        list.setDescription(description);

        // 4. Save to database
        ReadingListDAO listDAO = new ReadingListDAO();
        boolean success = listDAO.createList(list);

        // 5. Set a flash message and redirect back
        if (success) {
            session.setAttribute("listMessage", "New list '" + title + "' created successfully!");
        } else {
            session.setAttribute("listMessage", "Error: Could not create list.");
            // We should use an error message class here, but we'll skip for simplicity
        }
        
        response.sendRedirect("myLists.jsp");
    }
}