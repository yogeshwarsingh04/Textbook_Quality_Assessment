<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Get the token from the URL
    String token = request.getParameter("token");

    // Get Flash Message (if any)
    String message = (String) session.getAttribute("resetMessage");
    String msgClass = (String) session.getAttribute("messageClass");
    session.removeAttribute("resetMessage");
    session.removeAttribute("messageClass");
%>
<html>
<head>
    <title>Reset Password</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@picocss/pico@1/css/pico.min.css">
    <link rel="stylesheet" href="css/style.css">
    
    <script src="https://cdn.jsdelivr.net/npm/feather-icons/dist/feather.min.js"></script>
</head>
<body>

	

    <main class="container">
        <article>
            <h2 style="text-align: center;">Reset Your Password</h2>
            
            <% if (message != null) { %>
                <div class="message <%= msgClass %>"><%= message %></div>
            <% } %>
            
            <%-- This form will only show if there is a token in the URL --%>
            <% if (token != null && !token.isEmpty()) { %>
                <form action="resetPassword" method="POST">
                    
                    <%-- Send the token secretly --%>
                    <input type="hidden" name="token" value="<%= token %>">
                    
                    <label for="newPassword">New Password</label>
                    <input type="password" id="newPassword" name="newPassword" required>
                    
                    <label for="confirmPassword">Confirm New Password</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" required>
                    
                    <button type="submit">Set New Password</button>
                </form>
            <% } else { %>
                <div class="message error">
                    Error: Invalid or missing password reset token. Please request a new link.
                </div>
            <% } %>
            
        </article>
    </main>
    
    <script>
        feather.replace();
    </script>

</body>
</html>