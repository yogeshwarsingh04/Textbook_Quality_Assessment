<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Register</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@picocss/pico@1/css/pico.min.css">
    <link rel="stylesheet" href="css/style.css">
    
    <script src="https://cdn.jsdelivr.net/npm/feather-icons/dist/feather.min.js"></script>
</head>
<body>

	

    <main class="container center-form">
        <article>
            <h2 style="text-align: center;">Create Your Account</h2>
            
            <form action="register" method="POST">
                <label for="username">Username</label>
                <input type="text" id="username" name="username" required>
                
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required>
                
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required>
                
                <label for="role">I am a:</label>
                <select id="role" name="role">
                    <option value="student">Student</option>
                    <option value="educator">Educator</option>
                </select>
                
                <button type="submit">Register</button>
            </form>
            
            <footer style="text-align: center;">
                Already have an account? <a href="login.jsp">Login here</a>
            </footer>
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