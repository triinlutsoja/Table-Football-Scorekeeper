package com.football.Table_Football_Scorekeeper_API;

import java.util.Properties;

public class Profile {

    public static Properties getProperties(String name) {

        Properties props = new Properties();
        String env = System.getProperty("env");  // checks the environment for system property env
        if (env == null) {
            env = "dev";  // sets it to dev it not set
        }
        String propertiesFile = String.format("/config/%s.%s.properties", name, env);
        System.out.println(propertiesFile);

        try {
            props.load(TableFootballScorekeeperApiApplication.class.getResourceAsStream(propertiesFile));
        } catch (Exception e) {
            throw new RuntimeException("Cannot load properties file: " + propertiesFile);
        }
        return props;
    }
}
