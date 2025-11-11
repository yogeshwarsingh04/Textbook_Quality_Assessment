<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="com.textbook.dao.ReadingListDAO" %>
<%@ page import="com.textbook.model.ReadingList" %>
<%@ page import="com.textbook.model.Textbook" %>
<%@ page import="java.util.List" %>
<%@ page import="com.textbook.dao.UserDAO" %>
<%@ page import="com.textbook.model.User" %>

<%
    // Get User & List Info ---
    Integer userId = (Integer) session.getAttribute("userId");
    String username = (String) session.getAttribute("username");
    String role = (String) session.getAttribute("role");

    List<Textbook> bookList = null;
    ReadingList list = null; // We'll need to fetch this
    String listTitle = "Reading List"; // Default title
    int listId = 0;

    try {
        // Get the list ID from the URL
        listId = Integer.parseInt(request.getParameter("id"));
        
        ReadingListDAO listDAO = new ReadingListDAO();
        
        // Fetch all the books in this list
        bookList = listDAO.getBooksByListId(listId);
        
    } catch (NumberFormatException e) {
        // Handle bad ID
    }

%>

<html>
<head>
    <title><%= listTitle %></title> <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@picocss/pico@1/css/pico.min.css">
    <link rel="stylesheet" href="css/style.css">
    
    <script src="https://cdn.jsdelivr.net/npm/feather-icons/dist/feather.min.js"></script>
    
    <script>
        document.addEventListener('DOMContentLoaded', () => {
            const themeSwitcher = document.getElementById('theme-switcher');
            const themeLabel = document.getElementById('theme-label');
            if (!themeSwitcher) return;
            
            function setTheme(theme) {
                document.documentElement.setAttribute('data-theme', theme);
                localStorage.setItem('theme', theme);
                if (theme === 'light') {
                    themeSwitcher.checked = true;
                    if(themeLabel) themeLabel.textContent = 'Light Mode';
                } else {
                    themeSwitcher.checked = false;
                    if(themeLabel) themeLabel.textContent = 'Dark Mode';
                }
            }
            
            function handleToggle() {
                setTheme(themeSwitcher.checked ? 'light' : 'dark');
            }
            
            const savedTheme = localStorage.getItem('theme');
            if (savedTheme) {
                setTheme(savedTheme);
            } else {
                const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
                setTheme(prefersDark ? 'dark' : 'light');
            }
            
            themeSwitcher.addEventListener('change', handleToggle);
        });
    </script>
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
    
        <% if (bookList == null) { %>
            <article>
                <h2>List Not Found</h2>
                <p>The reading list you are looking for does not exist.</p>
                <a href="myLists.jsp" role="button" class="secondary">Back to My Lists</a>
            </article>
        <% } else { %>
            <h2>Books in this List</h2> 
            
            <figure style="overflow-x: auto;">
                <table>
                    <thead>
                        <tr>
                            <th>Title</th>
                            <th>Author</th>
                            <th>Subject</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (bookList.isEmpty()) { %>
                            <tr>
                                <td colspan="4">There are no books in this list yet.</td>
                            </tr>
                        <% } %>
                        <% for (Textbook book : bookList) { %>
                            <tr>
                                <td><%= book.getTitle() %></td>
                                <td><%= book.getAuthor() %></td>
                                <td><%= book.getSubject() %></td>
                                <td>
                                    <a href="bookDetails.jsp?id=<%= book.getBookId() %>" role="button" class="outline">View Details</a>
                                    
                                    <form action="removeFromList" method="POST" style="display: inline-block; margin-left: 5px;">
                                    	<input type="hidden" name="book_id" value="<%= book.getBookId() %>">
                                    	<input type="hidden" name="list_id" value="<%= listId %>">
                                    	<button type="submit" class="contrast outline">Remove</button>
                                	</form>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </figure>
            
            <a href="myLists.jsp" role="button" class="secondary">Back to All Lists</a>
        <% } %>
    </main>
    
    <script>
        feather.replace();
    </script>

</body>
</html>