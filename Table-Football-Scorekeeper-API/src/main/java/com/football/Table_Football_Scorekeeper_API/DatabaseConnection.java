package com.football.Table_Football_Scorekeeper_API;

import com.mysql.cj.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static DatabaseConnection db = new DatabaseConnection();
    private Connection conn;

    public static DatabaseConnection instance() {
        return db;
    }

    // private constructor, can't be used from outside the class
    private DatabaseConnection() {}

    /*public Connection getConnection() {  // Retrieves the established connection for use elsewhere
        return conn;
    }*/

    public Connection connect(Properties props) throws SQLException {  // Establishes a connection to the database using
        // the
        String server = props.getProperty("server");
        String port = props.getProperty("port");
        String database = props.getProperty("database");
        String user = props.getProperty("user");
        String password = props.getProperty("password");
        String driver = props.getProperty("driver");

        // The "address" to the MySQL database. Server, port and database replaced with %s, supply them as arguments
        String url = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC", server, port, database);

        // Load driver
        DriverManager.registerDriver(new Driver());

        // provided URL, username, and password.
        conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    public void close() throws SQLException {
        conn.close();
    }
}