package com.football.Table_Football_Scorekeeper_API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/*
It seems that Spring Boot is trying to set up a connection to a database, but it can’t find a suitable driver, which leads to the error about being unable to determine a “suitable driver class.”

This usually happens because Spring Boot auto-configures a DataSource when it detects JPA dependencies (like spring-boot-starter-data-jpa). Since you have not configured a database URL, username, or password, Spring Boot is assuming there should be an in-memory database like H2 or HSQL on the classpath—but it doesn’t find one.

Solution

Since you’re currently using in-memory repositories (without a real database), you should prevent Spring Boot from auto-configuring a DataSource or JPA.
*/

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@ComponentScan(basePackages = "com.football.Table_Football_Scorekeeper_API")
public class TableFootballScorekeeperApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TableFootballScorekeeperApiApplication.class, args);
	}

}
