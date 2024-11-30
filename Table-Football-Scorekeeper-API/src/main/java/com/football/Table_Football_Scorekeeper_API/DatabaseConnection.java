package com.football.Table_Football_Scorekeeper_API;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {

    private final DataSource dataSource;

    // Constructor to manually configure HikariDataSource
    public DatabaseConnection() {
        HikariConfig config = new HikariConfig();

        // Set your database connection details
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/table_football_db");
        config.setUsername("football_admin");
        config.setPassword("EvoconChamp");
        config.setMaximumPoolSize(1);
        config.setPoolName("HikariPool-1");
        config.setDriverClassName("org.postgresql.Driver");

        this.dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}