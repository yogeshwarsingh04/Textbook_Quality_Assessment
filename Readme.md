# 📚 Textbook Quality Assessment

The **Textbook Quality Assessment Tool** is a comprehensive, server-side web application designed to help educators, curriculum designers, and publishers systematically evaluate the quality of textbooks and other educational materials.

This tool provides a structured, multi-user framework based on established pedagogical criteria. It allows authenticated users to create detailed assessments, save their progress, and view past reports, all stored in a central database.

## ✨ Features

- **User Authentication:** Secure login and registration for [e.g., students, professors, and administrators].
- **Textbook Management:** [e.g., Ability to add, search, and view textbook details].
- **Quality Assessment:** [e.g., A dynamic rating system based on criteria like accuracy, clarity, and cost].
- **Review System:** [e.g., Users can write detailed qualitative reviews].

---

## 🛠️ Tech Stack

- **Backend:** Java (Servlets)
- **Frontend:** JSP (JavaServer Pages), HTML, CSS, JavaScript
- **Database:** MySQL
- **Server:** Apache Tomcat

---

## 🚀 Getting Started

Follow these instructions to get a copy of the project up and running on your local machine for development and testing.

### Prerequisites

You will need the following software installed on your machine:

- [Java Development Kit (JDK) 8 or higher](https://www.oracle.com/java/technologies/javase-downloads.html)
- [Apache Tomcat 9.0 or higher](https://tomcat.apache.org/)
- [MySQL Server](https://dev.mysql.com/downloads/mysql/)
- [Git](https://git-scm.com/)

### Installation

1.  **Clone the repository:**

    ```sh
    git clone https://github.com/yogeshwarsingh04/Textbook_Quality_Assessment.git
    cd Textbook_Quality_Assessment
    ```

2.  **Database Setup:**

    - Open your MySQL server and create a new database:
      ```sql
      CREATE DATABASE textbook_assessment_db;
      ```
    - Import the database schema. You can run the `schema.sql` file or create the tables manually.
      ```sh
      mysql -u [your-username] -p textbook_assessment_db < database/schema.sql
      ```

3.  **Configure the Application:**

    - Navigate to `src/main/java/com/textbook/util/DatabaseUtil.java`.
    - Update the database URL, username, and password with your MySQL credentials.

4.  **Deploy to Tomcat:**

    - Build your project into a `.war` (Web Archive) file. (If you are using an IDE like Eclipse or IntelliJ, you can do this by right-clicking the project).
    - Copy the generated `.war` file to the `webapps` directory of your Apache Tomcat installation.
    - Start the Tomcat server.

5.  **Access the Application:**
    Open your web browser and navigate to `http://localhost:8080/TextbookAssessment`

---

## Database Design

The database consists of [e.g., 4] main tables:

- **Users:** Stores user information (ID, username, password, role).
- **Textbooks:** Stores textbook details (ID, title, author, ISBN).
- **Experts:** Stores information about subject matter experts who can review books (ID, name, area_of_expertise, user_id).
- **Reviews:** Stores the qualitative comments from users.
- **ReadingLists:** Stores user-created collections or "lists" (e.g., "My Semester 1 Books") (ID, list_name, user_id).
- **ReadingListItems:** A join table that links specific textbooks to the reading lists they belong to (list_id, textbook_id).  

### Example Schema

Here is an example of what the `Users` and `Textbooks` tables might look like:

| **users**       |
| :-------------- | :------------------ |
| `user_id`       | `INT` (Primary Key) |
| `username`      | `VARCHAR(50)`       |
| `password_hash` | `VARCHAR(255)`      |
| `role`          | `VARCHAR(20)`       |

| **textbooks** |
| :------------ | :------------------ |
| `book_id`     | `INT` (Primary Key) |
| `title`       | `VARCHAR(255)`      |
| `author`      | `VARCHAR(100)`      |
| `isbn`        | `VARCHAR(13)`       |
| `subject`     | `VARCHAR(50)`       |

---

## 🧑‍💻 Author

- **Yogeshwar Singh**
  - https://github.com/yogeshwarsingh04
  - www.linkedin.com/in/contact-yogeshwarsingh


---
