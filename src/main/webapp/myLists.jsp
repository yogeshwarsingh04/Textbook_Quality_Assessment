<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.textbook.dao.ReadingListDAO" %>
<%@ page import="com.textbook.model.ReadingList" %>
<%@ page import="java.util.List" %>

<%
    // --- 1. Security Check ---
    Integer userId = (Integer) session.getAttribute("userId");
    String username = (String) session.getAttribute("username");
    String role = (String) session.getAttribute("role");
    
    // Only educators or admins can manage lists
    if (userId == null || (!role.equals("educator") && !role.equals("admin"))) {
        response.sendRedirect("index.jsp"); // Send non-educators away
        return;
    }
    
    // --- 2. Fetch User's Lists ---
    ReadingListDAO listDAO = new ReadingListDAO();
    List<ReadingList> myLists = listDAO.getListsByUserId(userId);
    
    // --- 3. Get Flash Message (if any) ---
    String message = (String) session.getAttribute("listMessage");
    session.removeAttribute("listMessage");
%>

<html>
<head>
    <title>My Reading Lists</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@picocss/pico@1/css/pico.min.css">
    
    <style>
        main.container { padding-top: 1rem; }
        .list-item { 
            display: flex; 
            justify-content: space-between; 
            align-items: center;
            margin-bottom: 0.5rem;
        }
        .message {
            padding: 1rem;
            margin-bottom: 1rem;
            border-radius: var(--pico-border-radius);
            background-color: var(--pico-positive-background);
            border: 1px solid var(--pico-positive-border);
            color: var(--pico-positive-color);
        }
    </style>
    
    <script>
        document.addEventListener('DOMContentLoaded', () => {
            const themeSwitcher = document.getElementById('theme-switcher');
            const themeLabel = document.getElementById('theme-label');
            if (!themeSwitcher) return; // In case it's not on a page
            
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
        
        <% if (message != null) { %>
            <div class="message"><%= message %></div>
        <% } %>

        <div class="grid">
            
            <article>
                <header><h3>Create a New List</h3></header>
                <form action="createList" method="POST">
                    <label for="title">List Title</label>
                    <input type="text" id="title" name="title" required>
                    
                    <label for="description">Description (Optional)</label>
                    <textarea id="description" name="description" rows="3"></textarea>
                    
                    <button type="submit">Create List</button>
                </form>
            </article>
            
            <article>
                <header><h3>Your Reading Lists</h3></header>
                <% if (myLists.isEmpty()) { %>
                    <p>You haven't created any lists yet.</p>
                <% } else { %>
                    <% for (ReadingList list : myLists) { %>
                        <div class="list-item">
                            
                            <strong><a href="viewList.jsp?id=<%= list.getListId() %>"><%= list.getTitle() %></a></strong>
                            <a href="viewList.jsp?id=<%= list.getListId() %>" role="button" class="secondary outline">View</a>
                        </div>
                    <% } %>
                <% } %>
            </article>
        
        </div>
    </main>

</body>
</html>