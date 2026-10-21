# CineBook — Movie Ticket Booking System

## Overview
CineBook is a command-line movie ticket booking application built using Java 21 and MySQL. It allows users to view movie shows, check seat availability, book tickets, apply discounts, view booking history, and cancel bookings.

## Technologies Used
- Java 21
- MySQL
- JDBC
- Maven
- MySQL Workbench

## Features
- View movies and showtimes
- View seat availability
- Book multiple seats
- Regular, Premium, and VIP ticket categories
- 10% discount using coupon code `CINE10`
- Booking history
- Cancellation with an 80% refund calculation
- MySQL database integration

## Requirements
- Java 21
- Maven
- MySQL Server

## Database Setup
1. Open MySQL Workbench and connect to your MySQL server.
2. Open `database.sql`.
3. Execute the script to create the database and restore its tables and data.
4. Ensure the database name is `cinebook`.

## Configure the Database Password
Set the `CINEBOOK_DB_PASSWORD` environment variable to your MySQL password in PowerShell:

```powershell
$env:CINEBOOK_DB_PASSWORD = "your_mysql_password"
```

## Run the Application
Open a terminal in the project directory and execute:

```powershell
mvn clean compile
mvn exec:java "-Dexec.mainClass=org.yourcompany.yourproject.CineApp"
```

## Database Connection Test
To test the MySQL connection, run:

```powershell
mvn exec:java "-Dexec.mainClass=org.yourcompany.yourproject.DBTest"
```