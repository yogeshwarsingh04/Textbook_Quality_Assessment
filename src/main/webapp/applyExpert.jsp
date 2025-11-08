<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Integer userId = (Integer) session.getAttribute("userId");
    if (userId == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    String username = (String) session.getAttribute("username");
    String role = (String) session.getAttribute("role");
%>
<html>
<head>
    <title>Apply for Expert Status</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@picocss/pico@1/css/pico.min.css">
    <style> main.container { padding-top: 1rem; max-width: 768px; } </style>
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
        <article>
            <h2>Apply to be an Expert Reviewer</h2>
            <p>Please submit your credentials and area of specialization. An administrator will review your application.</p>
            
            <form action="applyExpert" method="POST">
                <label for="full_name">Full Name</label>
                <input type="text" id="full_name" name="full_name" required>
                
                <label for="specialization">Area of Specialization (e.g., "Mathematics")</label>
                <input type="text" id="specialization" name="specialization" required>
                
                <label for="credentials">Credentials (e.g., "PhD in Physics")</label>
                <textarea id="credentials" name="credentials" rows="5" required></textarea>
                
                <button type="submit">Submit Application</button>
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
                themeLabel.textContent = 'Light Mode'; // <-- NEW
            } else {
                themeSwitcher.checked = false;
                themeLabel.textContent = 'Dark Mode';  // <-- NEW
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