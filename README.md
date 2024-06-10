# Task 2 - Web Application Backend Development

This project involves developing the backend part of a web application to maintain information about the hierarchical structure of an organization. The organization consists of directorates, departments, and employees. The application will ensure secure access to authenticated users and will provide APIs for CRUD operations on the data.

## Table of Contents

- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Endpoints](#endpoints)
- [Security Configuration](#security-configuration)
- [Method Security](#method-security)
- [JWT Authentication Filter](#jwt-authentication-filter)

### Technologies Used
- Java 17
- Maven 3.9.6
- PostgreSQL 16.3 : As the database system. 
- Spring Boot: To create stand-alone, production-grade Spring-based applications.
- Spring Security: To handle authentication and authorization.
- Spring Data JPA: To handle database operations.
- JWT (JSON Web Tokens): For securing the API endpoints.

## Getting Started

These instructions will help you set up and run the project on your local machine for development and testing purposes.

### Prerequisites

- Java 17 or higher
- Maven 3.9.6 or higher
- PostgreSQL database

### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/Mario-Giev/Mario-Giev-Infosystems-International-Task.git
    ```

2. Configure the database in `src/main/resources/application.properties`:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/database_name
    spring.datasource.username=your_database_username
    spring.datasource.password=your_database_password
    ```

3. Build the project using Maven:
    ```sh
    mvn clean install
    ```

4. Create the database using the script in /sql/tables.sql

5. Run the Spring Boot application:
    ```sh
    mvn spring-boot:run
    ```

## Usage

The application provides endpoints for authentication and accessing secured resources based on user roles.

### Endpoints

A Swagger Documentation is available for the endpoints at http://localhost:8080/swagger-ui/index.html

### Security Configuration
The security configuration ensures that all endpoints are secured and accessible only by authenticated users with appropriate roles.

### Method Security
Role-based access control is enforced using @PreAuthorize annotations on the controller methods.

### JWT Authentication Filter
The JwtAuthenticationFilter extracts the JWT token from the request header, validates it, and sets the user details in the security context.

### JWT Token Structure
The JWT token includes the user's role as a claim:

```json
{
    "role": "EMPLOYEE",
    "sub": "mario@mail.com",
    "iat": 1718001312,
    "exp": 1718606112
}
```