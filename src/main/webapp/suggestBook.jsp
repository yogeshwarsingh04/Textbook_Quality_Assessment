<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Integer userId = (Integer) session.getAttribute("userId");
    if (userId == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    String username = (String) session.getAttribute("username");
    String role = (String) session.getAttribute("role");
    
    String message = (String) session.getAttribute("suggestMessage");
    session.removeAttribute("suggestMessage");
%>
<html>
<head>
    <title>Suggest a New Textbook</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@picocss/pico@1/css/pico.min.css">
    <link rel="stylesheet" href="css/style.css">
    
    <script src="https://cdn.jsdelivr.net/npm/feather-icons/dist/feather.min.js"></script>
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

    <main class="container narrow-form">
        <article>
            <h2>Suggest a New Textbook</h2>
            <p>Your suggestion will be reviewed by an administrator before it appears on the site.</p>

            <% if (message != null) { %>
                <div class="message"><%= message %></div>
            <% } %>

            <form action="suggestBook" method="POST">
                <label for="title">Title</label>
                <input type="text" id="title" name="title" required>
                
                <label for="author">Author</label>
                <input type="text" id="author" name="author" required>
                
                <label for="isbn">ISBN (13-digit)</label>
                <input type="text" id="isbn" name="isbn" maxlength="13">
                
                <label for="publisher">Publisher</label>
                <input type="text" id="publisher" name="publisher">
                
                <label for="subject">Subject</label>
                <input type="text" id="subject" name="subject">
                
                <label for="grade_level">Grade Level</label>
                <input type="text" id="grade_level" name="grade_level">
                
                <label for="publication_year">Publication Year</label>
                <input type="number" id="publication_year" name="publication_year" min="1900" max="2100">
                
                <button type="submit">Submit Suggestion</button>
            </form>
        </article>
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
    
    <script>
        feather.replace();
    </script>
    
</body>
</html>