<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.textbook.dao.TextbookDAO" %>
<%@ page import="com.textbook.model.Textbook" %>
<%@ page import="com.textbook.dao.ReviewDAO" %>
<%@ page import="com.textbook.model.Review" %>
<%@ page import="com.textbook.dao.ReadingListDAO" %>
<%@ page import="com.textbook.model.ReadingList" %>
<%@ page import="com.textbook.model.RatingSummary" %>
<%@ page import="java.util.List" %>

<%
    Textbook book = null;
    List<Review> reviewList = null;
    RatingSummary summary = null;
    int bookId = 0;
    
    try {
        bookId = Integer.parseInt(request.getParameter("id"));
        TextbookDAO textbookDAO = new TextbookDAO();
        book = textbookDAO.getTextbookById(bookId);
        ReviewDAO reviewDAO = new ReviewDAO();
        reviewList = reviewDAO.getReviewsByBookId(bookId);
        summary = reviewDAO.getRatingSummary(bookId);
    } catch (NumberFormatException e) {
        // Handle error
    }
    
    String username = (String) session.getAttribute("username");
    Integer userId = (Integer) session.getAttribute("userId");
    String role = (String) session.getAttribute("role");
    
 	// Fetch the user's lists ONLY if they are an educator or admin
    List<ReadingList> userLists = null;
    if (userId != null && (role.equals("educator") || role.equals("admin"))) {
        ReadingListDAO listDAO = new ReadingListDAO();
        userLists = listDAO.getListsByUserId(userId);
    }
%>

<html>
<head>
    <title>Book Details</title>
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

    <main class="container">
    
    	<%
            String message = (String) session.getAttribute("bookDetailMessage");
            String msgClass = (String) session.getAttribute("messageClass");
            
            if (message != null) {
                // We only print if a message exists
                
                // Set a safe default for the class
                String finalClass = (msgClass == null) ? "success" : msgClass;
                
                // Print the HTML
                out.println("<div class='message " + finalClass + "'>" + message + "</div>");
                
                // And clear the attributes
                session.removeAttribute("bookDetailMessage");
                session.removeAttribute("messageClass");
            }
        %>
    
        <% if (book != null) { %>
        
            <article>
                <header>
                    <h2><%= book.getTitle() %></h2>
                    <strong>Author:</strong> <%= book.getAuthor() %> | 
                    <strong>Publisher:</strong> <%= book.getPublisher() %> | 
                    <strong>ISBN:</strong> <%= book.getIsbn() %>
                </header>
                <p>
                    <strong>Subject:</strong> <%= book.getSubject() %> | 
                    <strong>Grade Level:</strong> <%= book.getGradeLevel() %> | 
                    <strong>Year:</strong> <%= book.getPublicationYear() %>
                </p>
            </article>
            
            <article>
                <header><strong>Overall Rating Summary</strong></header>
                <% if (summary.getReviewCount() == 0) { %>
                    <p>This book has not been rated yet.</p>
                <% } else { %>
                    <h3 style="margin-bottom: 0;">
                        ⭐ <%= String.format("%.1f", summary.getOverallAverage()) %> / 5
                    </h3>
                    <small>Based on <%= summary.getReviewCount() %> review(s)</small>
                    
                    <hr>
                    <p><strong>Detailed Breakdown:</strong></p>
                    
                    <label for="acc">Accuracy
                        <small>(<%= String.format("%.1f", summary.getAverageAccuracy()) %>)</small>
                    </label>
                    <progress id="acc" value="<%= summary.getAverageAccuracy() %>" max="5"></progress>
                    
                    <label for="cla">Clarity
                        <small>(<%= String.format("%.1f", summary.getAverageClarity()) %>)</small>
                    </label>
                    <progress id="cla" value="<%= summary.getAverageClarity() %>" max="5"></progress>
                    
                    <label for="eng">Engagement
                        <small>(<%= String.format("%.1f", summary.getAverageEngagement()) %>)</small>
                    </label>
                    <progress id="eng" value="<%= summary.getAverageEngagement() %>" max="5"></progress>
                    
                    <label for="sen">Sensitivity
                        <small>(<%= String.format("%.1f", summary.getAverageSensitivity()) %>)</small>
                    </label>
                    <progress id="sen" value="<%= summary.getAverageSensitivity() %>" max="5"></progress>
                <% } %>
            </article>
            
            <%-- Only show this section if the user is an educator/admin AND they have lists --%>
            <% if (userLists != null && !userLists.isEmpty()) { %>
                <article>
                    <header><strong>Add to Reading List</strong></header>
                    <form action="addToList" method="POST">
                        
                        <%-- Send the book ID and the current page (for redirect) secretly --%>
                        <input type="hidden" name="book_id" value="<%= book.getBookId() %>">
                        <input type="hidden" name="return_url" value="bookDetails.jsp?id=<%= book.getBookId() %>">
                        
                        <label for="list_id">Select a list:</label>
                        <select id="list_id" name="list_id" required>
                            <% for (ReadingList list : userLists) { %>
                                <option value="<%= list.getListId() %>"><%= list.getTitle() %></option>
                            <% } %>
                        </select>
                        <button type="submit">Add Book</button>
                    </form>
                </article>
            <% } %>

            <section id="reviews">
                <h3>Reviews</h3>
                
                <% if (reviewList.isEmpty()) { %>
                    <p>No reviews yet. Be the first to submit one!</p>
                <% } else { %>
                    <% for (Review review : reviewList) { 
                        
                        String cardClass = "review-card"; 
                        String badge = "";
                        String reviewRole = review.getRole();
                        
                        if ("expert".equals(reviewRole)) {
                            cardClass += " expert-review";
                            badge = "<span class='badge expert-badge'>Expert Review</span>";
                        } else if ("educator".equals(reviewRole)) {
                            cardClass += " educator-review";
                            badge = "<span class='badge educator-badge'>Educator Review</span>";
                        }
                    %>
                        <article class="<%= cardClass %>">
                            <header>
                                <strong><%= review.getReviewTitle() %></strong>
                                <%= badge %>
                            </header>
                            <p><%= review.getReviewBody() %></p>
                            <footer>
                                <small>
                                    <strong>By:</strong> <%= review.getUsername() %> | 
                                    <strong>Accuracy:</strong> <%= review.getRatingAccuracy() %>/5 | 
                                    <strong>Clarity:</strong> <%= review.getRatingClarity() %>/5 | 
                                    <strong>Engagement:</strong> <%= review.getRatingEngagement() %>/5 | 
                                    <strong>Sensitivity:</strong> <%= review.getRatingSensitivity() %>/5
                                </small>
                            </footer>
                        </article>
                    <% } %>
                <% } %>
            </section>

            <section id="submit-review">
                <% if (userId != null) { %>
                    <article>
                        <h3>Submit Your Review</h3>
                        <form action="submitReview" method="POST">
                            <input type="hidden" name="book_id" value="<%= book.getBookId() %>">
                            
                            <label for="review_title">Review Title</label>
                            <input type="text" id="review_title" name="review_title" required>

                            <p><strong>Rate the Quality (1-5):</strong></p>
                            <div class="grid">
                                <div>
                                    <label for="rating_accuracy">Accuracy</label>
                                    <input type="number" id="rating_accuracy" name="rating_accuracy" min="1" max="5" required>
                                </div>
                                <div>
                                    <label for="rating_clarity">Clarity</label>
                                    <input type="number" id="rating_clarity" name="rating_clarity" min="1" max="5" required>
                                </div>
                                <div>
                                    <label for="rating_engagement">Engagement</label>
                                    <input type="number" id="rating_engagement" name="rating_engagement" min="1" max="5" required>
                                </div>
                                <div>
                                    <label for="rating_sensitivity">Sensitivity</label>
                                    <input type="number" id="rating_sensitivity" name="rating_sensitivity" min="1" max="5" required>
                                </div>
                            </div>
                            
                            <label for="review_body">Full Review</label>
                            <textarea id="review_body" name="review_body" rows="5"></textarea>
                            
                            <button type="submit">Submit Review</button>
                        </form>
                    </article>
                <% } else { %>
                    <p><strong>You must be <a href="login.jsp">logged in</a> to submit a review.</strong></p>
                <% } %>
            </section>

        <% } else { %>
            <article>
                <h2>Book Not Found</h2>
                <p>The book you are looking for does not exist.</p>
                <a href="index.jsp" role="button">Back to all textbooks</a>
            </article>
        <% } %>

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