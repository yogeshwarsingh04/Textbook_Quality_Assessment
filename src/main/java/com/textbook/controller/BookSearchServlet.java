package com.textbook.controller;

import com.textbook.dao.TextbookDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/searchSuggestions")
public class BookSearchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String query = request.getParameter("query");

        if (query == null || query.trim().length() < 2) {
            return; // Don't search for less than 2 characters
        }

        TextbookDAO textbookDAO = new TextbookDAO();
        List<String> suggestions = textbookDAO.getBookTitleSuggestions(query);

        // Set the content type to simple HTML
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Loop through the suggestions and print them as <option> tags
        for (String title : suggestions) {
            // We set the "value" of the option
            out.print("<option value=\"" + title + "\">");
        }
    }
}