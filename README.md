<p align="center">
  <img src="https://github.com/jason-vila/bookstudio/blob/master/src/main/webapp/images/bookstudio-logo.png" width="250" alt="BookStudio Logo" />
</p>

## Screenshots

### Dashboard Interface
<p align="center">
  <img src="https://github.com/jason-vila/bookstudio/blob/master/src/main/webapp/images/dashboard-view.png" width="800" alt="Dashboard Preview" />
</p>

### Loans Interface
<p align="center">
  <img src="https://github.com/jason-vila/bookstudio/blob/master/src/main/webapp/images/loan-view.png" width="800" alt="Dashboard Preview" />
</p>

### User Authentication
<p align="center">
  <img src="https://github.com/jason-vila/bookstudio/blob/master/src/main/webapp/images/login-view.png" width="800" alt="Login Preview" />
</p>

## Description

BookStudio is a comprehensive web-based library management system designed for managing book loans at a fictional university library. The primary focus is on streamlining the lending process with clear status tracking (loaned/returned).

The system provides functionalities such as:
- **User Authentication** (Login)
- **Dashboard** for managing:
  - Loans
  - Books
  - Authors
  - Courses
  - Publishers
  - Students
  - Users
  - Profile

The system includes **light and dark modes**.

There are two user roles:
- **Administrator**
- **Librarian**

## Purpose

This project was developed for personal growth as I am starting in web development. It serves as a learning platform to apply various technologies and design patterns in a real-world scenario.

## Database Connection Configuration

The MySQL connection is configured in:
```
/src/main/java/utils/MySqlConexion.java
```

Example of an optimized connection configuration:

```java
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConexion {
    // Database connection parameters
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost/bookstudio_db?useSSL=false&useTimezone=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password";
    
    public static Connection getConexion() {
        Connection con = null;
        try {
            // Register the JDBC driver
            Class.forName(DRIVER);
            
            // Establish connection
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Driver not installed! " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error: Database connection failed! " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: General exception: " + e.getMessage());
        }
        return con;
    }
}
```

## Technologies and Dependencies

### Web Libraries (Loaded via CDN)
- **jQuery**: v3.7.1
- **Moment.js**: v2.29.1
- **Bootstrap Bundle**: v5.3.0 (includes Popper.js functionality)
- **DataTables**:
  - Core: v2.1.8
  - Bootstrap 5 integration: v2.1.8
  - Responsive extension: v3.0.3
  - Responsive Bootstrap 5 integration: v3.0.3
- **Popper.js**: v2.11.6 (if not using Bootstrap Bundle)
- **Bootstrap-select**: v1.14.0-beta3 (with Spanish localization via defaults-es_ES)
- **Cropper.js**

### Maven Dependencies
- **Gson** (for JSON processing): 2.8.9
- **Standard Taglibs**: 1.1.2
- **MySQL Connector/J**: 8.0.33
- **Protobuf Java**: 3.25.1

### Additional Technologies
- **Programming Language**: Java (code is in English; content is in Spanish)
- **Web Components**: Servlets, DAO, Ajax, and jQuery
- **Build Tool**: Maven
- **Application Server**: Tomcat 8.5
- **Database**: MySQL (access via JDBC)

## Installation and Setup

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/Jason-Vila/bookstudio.git
   ```

2. **Import the Project in Eclipse:**
   - Open Eclipse.
   - Navigate to `File > Import > Existing Maven Projects`.
   - Browse to the cloned repository folder and import the project.

3. **Configure the Database:**
   - Ensure MySQL is installed.
   - Create a database named `bookstudio_db`.
   - Update the connection parameters in `MySqlConexion.java` with your MySQL credentials.

4. **Run the Application:**
   - Start Tomcat 8.5 from Eclipse or deploy the project to your Tomcat server.
   - Access the application via your browser at `http://localhost:8080/bookstudio` (or your configured context path).

## Usage

- **Login:** Use the login page to authenticate.
- **Dashboard:** Once logged in, access the dashboard to manage loans, books, authors, courses, publishers, students, users, and your profile.
- **User Roles:** The system supports two roles (Administrator and Librarian) with appropriate permissions for each role.

## Contributing

This is a personal project aimed at learning and growth, but contributions are welcome. If you have suggestions, improvements, or bug fixes, feel free to fork the repository and submit a pull request.

## License

This project is not licensed.
