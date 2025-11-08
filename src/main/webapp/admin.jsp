<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.textbook.dao.ExpertDAO" %>
<%@ page import="com.textbook.model.Expert" %>
<%@ page import="java.util.List" %>
<%@ page import="com.textbook.dao.TextbookDAO" %>
<%@ page import="com.textbook.model.Textbook" %>
<%
    String role = (String) session.getAttribute("role");
    if (role == null || !role.equals("admin")) {
        response.sendRedirect("index.jsp");
        return;
    }
    ExpertDAO expertDAO = new ExpertDAO();
    List<Expert> pendingExpertList = expertDAO.getPendingApplications();
    TextbookDAO textbookDAO = new TextbookDAO();
    List<Textbook> pendingBookList = textbookDAO.getPendingTextbooks();
    String username = (String) session.getAttribute("username");
%>
<html>
<head>
    <title>Admin Panel</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@picocss/pico@1/css/pico.min.css">
    
    <style>
        main.container { padding-top: 1rem; }
        table { margin-top: 1rem; }
        .creds { max-width: 300px; white-space: pre-wrap; word-wrap: break-word; }
        
        /* We can make these look a bit more like Pico buttons */
        .approve, .reject {
            padding: 5px 10px;
            cursor: pointer;
            border: none;
            border-radius: var(--pico-border-radius);
            color: white;
            font-size: 0.9em;
        }
        .approve { background-color: #28a745; }
        .reject { background-color: #dc3545; }
    </style>
</head>
<body>

    <nav class="container-fluid">
        <ul>
            <li><a href="index.jsp" class="secondary"><strong>Textbook Assessment</strong></a></li>
        </ul>
        <ul>
            <% if (username != null) { %>
                <li><a href="profile.jsp">My Profile</a></li>
                <li><a href="suggestBook.jsp">Suggest Book</a></li>
                
                <% if ("admin".equals(role)) { %>
                    <li><a href="admin.jsp" role="button" class="contrast">Admin Panel</a></li>
                <% } else if ("educator".equals(role)) { %>
                    <%-- ## NEW LINK FOR EDUCATORS ## --%>
                    <li><a href="myLists.jsp">My Reading Lists</a></li> 
                <% } else { %>
                    <li><a href="applyExpert.jsp">Apply for Expert</a></li>
                <% } %>
                
                <li><a href="logout" role="button" class="secondary">Logout</a></li>
            <% } else { %>
                <li><a href="login.jsp" role="button">Login</a></li>
                <li><a href="register.jsp" role="button" class="contrast">Register</a></li>
            <% } %>
            <li>
                <label for="theme-switcher">
                    <input type="checkbox" id="theme-switcher" name="theme-switcher" role="switch">
                    <span id="theme-label">Light Mode</span>
                </label>
            </li>
        </ul>
    </nav>

    <main class="container">
        <h2>Pending Expert Applications</h2>
        
        <figure style="overflow-x: auto;">
            <table>
                <thead>
                    <tr>
                        <th>Username</th>
                        <th>Full Name</th>
                        <th>Specialization</th>
                        <th>Credentials</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Expert app : pendingExpertList) { %>
                        <tr>
                            <td><%= app.getUsername() %></td>
                            <td><%= app.getFullName() %></td>
                            <td><%= app.getSpecialization() %></td>
                            <td class="creds"><%= app.getCredentials() %></td>
                            <td>
                                <form action="manageApplication" method="POST" style="display:inline;">
                                    <input type="hidden" name="userId" value="<%= app.getExpertId() %>">
                                    <input type="hidden" name="action" value="approved">
                                    <button type="submit" class="approve">Approve</button>
                                </form>
                                <form action="manageApplication" method="POST" style="display:inline;">
                                    <input type="hidden" name="userId" value="<%= app.getExpertId() %>">
                                    <input type="hidden" name="action" value="rejected">
                                    <button type="submit" class="reject">Reject</button>
                                </form>
                            </td>
                        </tr>
                    <% } %>
                    <% if (pendingExpertList.isEmpty()) { %>
                        <tr>
                            <td colspan="5">No pending applications.</td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </figure>
        
        <h2>Pending Textbook Suggestions</h2>
        <figure style="overflow-x: auto;">
            <table>
                <thead>
                    <tr>
                        <th>Title</th>
                        <th>Author</th>
                        <th>ISBN</th>
                        <th>Subject</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Textbook book : pendingBookList) { %>
                        <tr>
                            <td><%= book.getTitle() %></td>
                            <td><%= book.getAuthor() %></td>
                            <td><%= book.getIsbn() %></td>
                            <td><%= book.getSubject() %></td>
                            <td>
                                <form action="manageBook" method="POST" style="display:inline;">
                                    <input type="hidden" name="bookId" value="<%= book.getBookId() %>">
                                    <input type="hidden" name="action" value="approved">
                                    <button type="submit" class="approve">Approve</button>
                                </form>
                                <form action="manageBook" method="POST" style="display:inline;">
                                    <input type="hidden" name="bookId" value="<%= book.getBookId() %>">
                                    <input type="hidden" name="action" value="rejected">
                                    <button type="submit" class="reject">Reject</button>
                                </form>
                            </td>
                        </tr>
                    <% } %>
                    <% if (pendingBookList.isEmpty()) { %>
                        <tr>
                            <td colspan="5">No pending book suggestions.</td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </figure>
    </main>
    
    <script>
        // Get the theme-switcher checkbox
        const themeSwitcher = document.getElementById('theme-switcher');
        const themeLabel = document.getElementById('theme-label');
        
        // Function to set the theme
        function setTheme(theme) {
            document.documentElement.setAttribute('data-theme', theme);
            localStorage.setItem('theme', theme); // Save the choice
            
            // Update the checkbox and the label
            if (theme === 'light') {
                themeSwitcher.checked = true;
                themeLabel.textContent = 'Light Mode';
            } else {
                themeSwitcher.checked = false;
                themeLabel.textContent = 'Dark Mode';
            }
        }
        
        // Function to handle the toggle click
        function handleToggle() {
            if (themeSwitcher.checked) {
                setTheme('light');
            } else {
                setTheme('dark');
            }
        }
        
        // Load the saved theme on page load
        
        // Get the saved theme from localStorage
        const savedTheme = localStorage.getItem('theme');
        
        if (savedTheme) {
            // If there's a saved theme, use it
            setTheme(savedTheme);
        } else {
            // Otherwise, default to the browser's preference
            // and update the checkbox to match
            const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
            if (prefersDark) {
                setTheme('dark');
            } else {
                setTheme('light');
            }
        }
        
        // Listen for clicks on the toggle switch
        themeSwitcher.addEventListener('change', handleToggle);
        
    </script>

</body>
</html>