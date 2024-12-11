package com.football.Table_Football_Scorekeeper_API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;


@SpringBootApplication
public class TableFootballScorekeeperApiApplication {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		SpringApplication.run(TableFootballScorekeeperApiApplication.class, args);

		// Load JDBC class for MySQL driver. (not always required since JDBC 4.0+ automatically loads drivers)
		Class.forName("com.mysql.cj.jdbc.Driver");

		// create a single instance of the Database class (Singleton Pattern)
		DatabaseConnection db = DatabaseConnection.instance();  // one AND only instance of the db connection

		// try to establish connection
		try {
			db.connect();
			System.out.println("Connected.");
		} catch (SQLException e) {
			System.out.println("Cannot connect to database.");
		}

		// try to close connection
		try {
			db.close();
			System.out.println("Disconnected.");
		} catch (SQLException e) {
			System.out.println("Cannot close the database connection.");
		}
	}
}
