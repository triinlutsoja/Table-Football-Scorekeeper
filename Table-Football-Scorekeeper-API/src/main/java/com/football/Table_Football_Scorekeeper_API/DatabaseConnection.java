package com.football.Table_Football_Scorekeeper_API;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection db = new DatabaseConnection();

    // The "address" to the MySQL database
    private static final String dbUrl = "jdbc:mysql://localhost:3306/table_football_db?serverTimezone=UTC";

    private Connection conn;

    public static DatabaseConnection instance() {
        return db;
    }

    // private constructor, can't be used from outside the class
    private DatabaseConnection() {}

    public Connection getConnection() {  // Retrieves the established connection for use elsewhere
        return conn;
    }

    public void connect() throws SQLException {  // Establishes a connection to the database using the provided URL, username, and password.
        conn = DriverManager.getConnection(dbUrl, "root", "MinuMySQLpar00l");
    }

    public void close() throws SQLException {
        conn.close();
    }
}