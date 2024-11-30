package com.football.Table_Football_Scorekeeper_API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class TableFootballScorekeeperApiApplication implements CommandLineRunner {

	@Autowired
	private DatabaseConnection testDatabaseConnection;

	//@Autowired
	//private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(TableFootballScorekeeperApiApplication.class, args);
	}

	@Override
	public void run(String... args) {
		// Test the database connection on startup
		testDatabaseConnection.testConnection();
	}

	/*@PostConstruct
	public void init() {
		createTableIfNotExists();
	}*/

	/*public void createTableIfNotExists() {
		String sql = "CREATE TABLE IF NOT EXISTS player (player_id SERIAL PRIMARY KEY, name VARCHAR(255))";
		jdbcTemplate.execute(sql);
	}*/

}
