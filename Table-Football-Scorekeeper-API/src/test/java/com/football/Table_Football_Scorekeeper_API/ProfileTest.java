package com.football.Table_Football_Scorekeeper_API;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Properties;

public class ProfileTest {

    @Test
    public void testLoadDbConfig() {
        Properties props = Profile.getProperties("db");
        Assertions.assertNotNull(props, "Cannot load db properties.");
        var dbName = props.getProperty("database");
        Assertions.assertEquals("table_football_db_test", dbName, "dbName incorrect");
    }
}
