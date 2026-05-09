# Movie Ticket Reservation System (Java + MySQL)

I survived CIS611, and all I got were these code files…

A Java-based movie theater reservation system built with Nate Bullock using Java Swing, MySQL, and a 3-tier client-server architecture. Features include ticket booking, user authentication, payment validation, and DBA management tools.

---

## Overview

This project is a Java desktop application designed for a local movie theater to manage ticket reservations through a client-server architecture. The system allows customers to browse movie showtimes, reserve tickets, manage reservations, and process payments through a graphical user interface.

The project also includes a dedicated DBA/Admin interface for managing movies and showtimes.

This application was developed as the final term project for CIS611. :contentReference[oaicite:0]{index=0}

---

## Features

### Customer Features
- User sign up and login system
- Browse available movie showtimes by date
- Reserve multiple movie tickets
- View reservations by date
- Cancel reservations (must be at least 1 hour before showtime)
- Credit card validation
- Ticket confirmation display

### DBA/Admin Features
- Add movies
- Update movies
- Delete movies
- Add showtimes
- Update showtimes
- Delete showtimes
- View all movies and shows

### Technical Features
- 3-tier client/server architecture
- Java Swing GUI interfaces
- MySQL database integration
- Object-oriented programming design
- Client-server socket communication
- Multi-threaded server handling
- Input validation and error handling

---

## Technologies Used

- Java
- Java Swing
- MySQL
- JDBC
- Socket Programming
- Object-Oriented Programming (OOP)

---

## Project Structure

```text
src/
├── CustomerClient.java
├── DBAClient.java
├── DBConnection.java
├── LoginClient.java
├── Message.java
├── Movie.java
├── MovieShow.java
├── Reservation.java
├── Role.java
├── SignUpClient.java
├── TheaterDAO.java
├── TheaterServer.java
└── User.java
```

---

## System Architecture

This project follows a 3-tier client-server model:

1. Client Layer
   - Java Swing GUI applications
   - Customer and DBA interfaces

2. Server Layer
   - Handles business logic
   - Processes requests from clients
   - Communicates with the database

3. Database Layer
   - MySQL database
   - Stores users, movies, shows, reservations, and payments

---

## How to Run

### 1. Clone the Repository

```bash
git clone https://github.com/JKINGLIS/Java-Movie-Theater-Reservation-System.git
```

### 2. Open the Project

Import the project into:
- Eclipse
- IntelliJ IDEA
- VS Code

### 3. Configure the Database

Update the database credentials inside:

```java
DBConnection.java
```

Make sure your MySQL server is running and the required tables are created.

### 4. Start the Server

Run:

```java
TheaterServer.java
```

### 5. Launch the Client

Run:

```java
LoginClient.java
```

---

## Validation Rules

- Credit card numbers must be exactly 5 digits
- Phone numbers must contain 10 digits
- Usernames and passwords require minimum lengths
- Reservations cannot exceed available seats
- Reservations cannot be canceled less than 1 hour before showtime

---

## Future Improvements

- Password encryption
- Seat selection system
- Better payment processing
- Email notifications
- Modern UI redesign
- REST API implementation
- Cloud database deployment

---

## Authors

- Kahei Inglis
- Nate Bullock

---

## Acknowledgments

Developed for the CIS611 Final Term Project assignment focused on:
- Java GUI development
- Client-server architecture
- Database systems
- Object-oriented programming
- Software engineering principles

---

## License

This project is for educational purposes.
