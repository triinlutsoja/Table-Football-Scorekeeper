package com.football.Table_Football_Scorekeeper_API;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection db = new DatabaseConnection();

    public static DatabaseConnection instance() {
        return db;
    }

    // private constructor, can't be used from outside the class
    private DatabaseConnection() {}

    public Connection connect() throws SQLException {
        String url = "jdbc:sqlite:table_football_db.db";
        return DriverManager.getConnection(url);
    }
}