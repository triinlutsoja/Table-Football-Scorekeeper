package com.football.Table_Football_Scorekeeper_API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.TimeZone;

@SpringBootApplication
// @CrossOrigin(origins = "http://localhost:5500") // Global CORS setup
public class TableFootballScorekeeperApiApplication {

	public static void main(String[] args) {

        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Tallinn"));
		SpringApplication.run(TableFootballScorekeeperApiApplication.class, args);
	}
}
