package com.football.Table_Football_Scorekeeper_API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class TableFootballScorekeeperApiApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Tallinn"));
		SpringApplication.run(TableFootballScorekeeperApiApplication.class, args);
	}
}
