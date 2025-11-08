<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.textbook.dao.TextbookDAO" %>
<%@ page import="com.textbook.model.Textbook" %>
<%@ page import="java.util.List" %>
<%
    String searchQuery = request.getParameter("search");
    TextbookDAO textbookDAO = new TextbookDAO();
    List<Textbook> textbookList;
    
    if (searchQuery != null && !searchQuery.trim().isEmpty()) {
        textbookList = textbookDAO.searchTextbooks(searchQuery);
    } else {
        textbookList = textbookDAO.getAllTextbooks();
    }
    
    String username = (String) session.getAttribute("username");
    String role = (String) session.getAttribute("role");
%>
<html>
<head>
    <title>Textbook Assessment</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@picocss/pico@1/css/pico.min.css">
    <style>
        /* Small style to make header links vertical on small screens */
        @media (max-width: 768px) {
            nav ul li { margin-bottom: 0.5rem; }
        }
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
        
        <form action="index.jsp" method="GET" style="margin-top: 1rem;">
            <div class="grid">
                <input type="text" 
                       name="search" 
                       id="search-input" 
                       list="book-suggestions"
                       autocomplete="off"
                       placeholder="Search by title, author, or subject..."
                       value="<%= (searchQuery != null ? searchQuery : "") %>">
                <button type="submit">Search</button>
            </div>
            
            <datalist id="book-suggestions"></datalist>
        </form>

        <article style="margin-top: 1rem;">
            <% if (searchQuery != null && !searchQuery.trim().isEmpty()) { %>
                <h2>Search Results for "<%= searchQuery %>"</h2>
                <a href="index.jsp">Clear search and show all books</a>
            <% } else { %>
                <h2>Available Textbooks</h2>
            <% } %>
            
            <figure style="overflow-x: auto;">
                <table>
                    <thead>
                        <tr>
                            <th>Title</th>
                            <th>Author</th>
                            <th>Subject</th>
                            <th>Grade Level</th>
                            <th>Rating</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Textbook book : textbookList) { %>
                            <tr>
                                <td><%= book.getTitle() %></td>
                                <td><%= book.getAuthor() %></td>
                                <td><%= book.getSubject() %></td>
                                <td><%= book.getGradeLevel() %></td>
                                <td>
                        			<%-- Only show rating if it's > 0 (meaning it has reviews) --%>
                        			<% if (book.getAverageRating() > 0) { %>
                            			<%= String.format("%.1f", book.getAverageRating()) %>⭐ / 5
                        			<% } else { %>
                            			No reviews
                        			<% } %>
                    			</td>
                    			
                                <td><a href="bookDetails.jsp?id=<%= book.getBookId() %>">View Details</a></td>
                            </tr>
                        <% } %>
                        
                        <% if (textbookList.isEmpty()) { %>
                            <tr>
                                <td colspan="5">No textbooks found.</td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </figure>
        </article>
    </main>
    
    </main>

    <script>
        // Get our HTML elements
        const searchInput = document.getElementById('search-input');
        const suggestionsDatalist = document.getElementById('book-suggestions');

        // Listen for when the user types
        searchInput.addEventListener('keyup', (e) => {
            const query = searchInput.value;

            // Only search if 2 or more characters are typed
            if (query.length < 2) {
                suggestionsDatalist.innerHTML = ''; // Clear old suggestions
                return;
            }

            // Fetch new suggestions from our servlet
            fetch('searchSuggestions?query=' + query)
                .then(response => response.text()) // Get the response as HTML
                .then(html => {
                    // Update the datalist with the new <option> tags
                    suggestionsDatalist.innerHTML = html;
                })
                .catch(err => {
                    console.error('Error fetching suggestions:', err);
                });
        });
    </script>	
    
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