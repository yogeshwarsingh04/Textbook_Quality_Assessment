<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.textbook.dao.UserDAO" %>
<%@ page import="com.textbook.model.User" %>
<%
    Integer userId = (Integer) session.getAttribute("userId");
    if (userId == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    UserDAO userDAO = new UserDAO();
    User user = userDAO.getUserById(userId);
    String username = user.getUsername();
    String role = user.getRole();
    
    String message = (String) session.getAttribute("profileMessage");
    session.removeAttribute("profileMessage");
%>
<html>
<head>
    <title>My Profile</title>
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
        <h2>Account Settings</h2>

        <% if (message != null) { 
            String msgClass = message.startsWith("Error:") ? "error" : "success";
        %>
            <div class="message <%= msgClass %>">
                <%= message %>
            </div>
        <% } %>

        <article>
            <header><strong>Your Details</strong></header>
            <p><strong>Username:</strong> <%= user.getUsername() %></p>
            <p><strong>Email:</strong> <%= user.getEmail() %></p>
            <p><strong>Role:</strong> <%= user.getRole() %></p>
        </article>

        <article>
            <header><strong>Change Username</strong></header>
            <form action="updateUsername" method="POST">
                <label for="newUsername">New Username</label>
                <input type="text" id="newUsername" name="newUsername" required>
                <button type="submit">Update Username</button>
            </form>
        </article>
        
        <article>
            <header><strong>Change Email</strong></header>
            <form action="updateEmail" method="POST">
                <label for="newEmail">New Email</label>
                <input type="text" id="newEmail" name="newEmail" required>
                <button type="submit">Update Email</button>
            </form>
        </article>

        <article>
            <header><strong>Change Password</strong></header>
            <form action="updatePassword" method="POST">
                <label for="oldPassword">Old Password</label>
                <input type="password" id="oldPassword" name="oldPassword" required>
                
                <label for="newPassword">New Password</label>
                <input type="password" id="newPassword" name="newPassword" required>
                
                <label for="confirmPassword">Confirm New Password</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required>
                
                <button type="submit">Update Password</button>
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