<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Get Flash Message (if any)
    String message = (String) session.getAttribute("resetMessage");
    String msgClass = (String) session.getAttribute("messageClass");
    session.removeAttribute("resetMessage");
    session.removeAttribute("messageClass");
%>
<html>
<head>
    <title>Forgot Password</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@picocss/pico@1/css/pico.min.css">
    
    <style>
        main.container {
            display: flex;
            flex-direction: column;
            justify-content: center;
            min-height: 80vh;
            max-width: 500px;
        }
        /* Custom styles for messages */
        .message {
            padding: 1rem;
            margin-bottom: 1rem;
            border-radius: var(--pico-border-radius);
            border: 1px solid;
        }
        .success {
            background-color: var(--pico-positive-background);
            border-color: var(--pico-positive-border);
            color: var(--pico-positive-color);
        }
        .error {
            background-color: var(--pico-form-invalid-background);
            border-color: var(--pico-form-invalid-border);
            color: var(--pico-del-color);
        }
    </style>
</head>
<body>

	

    <main class="container">
        <article>
            <h2 style="text-align: center;">Forgot Password</h2>
            <p>Enter your email address and we'll send you a link to reset your password.</p>
            
            <% if (message != null) { %>
                <div class="message <%= msgClass %>"><%= message %></div>
            <% } %>
            
            <form action="forgotPassword" method="POST">
                
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required>
                
                <button type="submit">Send Reset Link</button>
            </form>
            
            <footer style="text-align: center;">
                Remembered your password? <a href="login.jsp">Login here</a>
            </footer>
        </article>
    </main>

</body>
</html>