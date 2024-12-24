package com.football.Table_Football_Scorekeeper_API.Repositories;

import com.football.Table_Football_Scorekeeper_API.Entities.Player;
import com.football.Table_Football_Scorekeeper_API.DatabaseConnection;
import com.football.Table_Football_Scorekeeper_API.Profile;
import com.football.Table_Football_Scorekeeper_API.TableFootballScorekeeperApiApplication;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Repository
public class PlayerRepositoryImpl implements PlayerRepository {

    private final DatabaseConnection db;

    /*static {  // TODO: Not necessary, gets loaded automatically
        try {
            // Load JDBC class for MySQL driver. (not always required since JDBC 4.0+ automatically loads drivers)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found", e);
        }
    }*/

    public PlayerRepositoryImpl() {
        // fetch a single instance of the DatabaseConnection class (Singleton Pattern)
        db = DatabaseConnection.instance(); // one AND only instance of the db connection
    }

    @Override
    public Player addPlayer(Player player) {
        Properties props = Profile.getProperties("db");

        // try to establish connection
        try {
            db.connect(props);
            System.out.println("Connected.");  // TODO: remove printing to console.
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database: " + e.getMessage(), e);
        }

        // Get connection, execute action, add player to the database
        try (Connection conn = db.getConnection();  // fetching the existing connection from DatabaseConnection
             PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO player (name) VALUES(?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            insertStmt.setString(1, player.getName());
            insertStmt.executeUpdate();

            // Retrieve the auto-generated ID
            ResultSet generatedKeys = insertStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                Long generatedId = generatedKeys.getLong(1);  // The first column is the generated key

                // Query the database to fetch the just added player and return it
                PreparedStatement selectStmt = conn.prepareStatement("SELECT id, name FROM player WHERE id = ?");
                selectStmt.setLong(1, generatedId);
                ResultSet rs = selectStmt.executeQuery();
                if (rs.next()) {
                    Player retrievedPlayer = new Player();
                    Long retrievedId = rs.getLong("id");
                    String retrievedName = rs.getString("name");
                    retrievedPlayer.setId(retrievedId);
                    retrievedPlayer.setName(retrievedName);
                    return retrievedPlayer;
                }
                selectStmt.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred during SQL operation: " + e.getMessage(), e);
        }
        // If adding a new player fails, throw an exception
        throw new RuntimeException("Failed to add player to the database.");
    }

    @Override
    public Optional<Player> getPlayer(Long id) {

        Properties props = Profile.getProperties("db");

        // try to establish connection
        try {
            db.connect(props);
            System.out.println("Connected.");  // TODO: remove printing to console.
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database: " + e.getMessage(), e);
        }

        // Get connection, execute query, get player from the database. Try-with-resources closes stuff automatically
        try (Connection conn = db.getConnection();  // fetching the existing connection from DatabaseConnection
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM player WHERE id = ?")) {

            stmt.setLong(1, id); // Set the ID parameter in the query
            ResultSet rs = stmt.executeQuery(); // Execute the query

            if (rs.next()) { // Check if a result is returned
                // Create a Player object from the result set
                Player retrievedPlayer = new Player();
                retrievedPlayer.setId(rs.getLong("id")); // Map the "id" column to id
                retrievedPlayer.setName(rs.getString("name")); // Map the "name" column to name
                return Optional.of(retrievedPlayer); // Return the Player wrapped in an Optional
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred during SQL operation: " + e.getMessage(), e);
        }
        return Optional.empty(); // Return empty Optional if no player with the given id is found
    }

    @Override
    public List<Player> getAllPlayers() {

        Properties props = Profile.getProperties("db");

        // try to establish connection
        try {
            db.connect(props);
            System.out.println("Connected.");  // TODO: remove printing to console.
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database: " + e.getMessage(), e);
        }

        List<Player> allPlayers = new ArrayList<>();

        // Get connection, execute query, get player from the database. Try-with-resources closes stuff automatically
        try (Connection conn = db.getConnection();  // fetching the existing connection from DatabaseConnection
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM player")) {

            ResultSet rs = stmt.executeQuery(); // Execute the query
            while (rs.next()) { // Check if a result is returned
                // Create a Player object from the result set
                Player retrievedPlayer = new Player();
                retrievedPlayer.setId(rs.getLong("id")); // Map the "id" column to id
                retrievedPlayer.setName(rs.getString("name")); // Map the "name" column to name
                allPlayers.add(retrievedPlayer);
            }
            return allPlayers;  // if no results, list stays empty
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred during SQL operation: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Player> updatePlayer(Long id, Player player) {

        Properties props = Profile.getProperties("db");

        // try to establish connection
        try {
            db.connect(props);
            System.out.println("Connected.");  // TODO: remove printing to console.
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database: " + e.getMessage(), e);
        }

        try (Connection conn = db.getConnection()) {  // fetching the existing connection from DatabaseConnection

            // Check if the player exists
            try (PreparedStatement selectStmt = conn.prepareStatement("SELECT * FROM player WHERE id = ?")) {
                selectStmt.setLong(1, id);
                ResultSet rs = selectStmt.executeQuery();
                if (!rs.next()) {
                    return Optional.empty(); // If no such player exists, return empty
                }
            }

            // Update the existing player
            try (PreparedStatement updateStmt = conn.prepareStatement("UPDATE player SET name = ? WHERE id = ?")) {
                updateStmt.setString(1, player.getName());
                updateStmt.setLong(2, id);
                updateStmt.executeUpdate();
            }

            // Retrieve updated player from the database
            try (PreparedStatement retrieveStmt = conn.prepareStatement("SELECT * FROM player WHERE id = ?")) {
                retrieveStmt.setLong(1, id); // Set the ID parameter in the query
                ResultSet rs = retrieveStmt.executeQuery(); // Execute the query

                if (rs.next()) {
                    Player updatedPlayer = new Player();
                    updatedPlayer.setId(rs.getLong("id")); // Map the "id" column to id
                    updatedPlayer.setName(rs.getString("name")); // Map the "name" column to name
                    return Optional.of(updatedPlayer); // Return the Player wrapped in an Optional
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred during SQL operation: " + e.getMessage(), e);
        }
        return Optional.empty(); // Return empty if not possible to return
    }

    @Override
    public boolean deletePlayer(Long id) {

        Properties props = Profile.getProperties("db");

        // try to establish connection
        try {
            db.connect(props);
            System.out.println("Connected.");  // TODO: remove printing to console.
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database: " + e.getMessage(), e);
        }

        try (Connection conn = db.getConnection()) {  // fetching the existing connection from DatabaseConnection

            // Check if the player exists
            try (PreparedStatement selectStmt = conn.prepareStatement("SELECT 1 FROM player WHERE id = ?")) {
                selectStmt.setLong(1, id);
                ResultSet rs = selectStmt.executeQuery();
                if (!rs.next()) {
                    return false; // If no such player exists, return false
                }
            }
            // Delete the existing player
            try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM player WHERE id = ?")) {
                deleteStmt.setLong(1, id); // Set parameters for the DELETE statement
                int rowsAffected = deleteStmt.executeUpdate(); // Execute the DELETE statement
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting player with " + id + ": " + e.getMessage());
            throw new RuntimeException("Error occurred during SQL operation: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Player> findByNameIgnoreCase(String name) {

        Properties props = Profile.getProperties("db");

        // try to establish connection
        try {
            db.connect(props);
            System.out.println("Connected.");  // TODO: remove printing to console.
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database: " + e.getMessage(), e);
        }

        List<Player> playersWithSpecificName = new ArrayList<>();

        // Get connection, execute query, get player from the database. Try-with-resources closes stuff automatically
        try (Connection conn = db.getConnection();  // fetching the existing connection from DatabaseConnection
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM player WHERE LOWER(name) LIKE LOWER(?)")) {

            stmt.setString(1, "%" + name + "%"); // Also check whether name is a substring
            ResultSet rs = stmt.executeQuery(); // Execute the query

            while (rs.next()) { // Check if a result is returned
                // Create a Player object from the result set
                Player retrievedPlayer = new Player();
                retrievedPlayer.setId(rs.getLong("id")); // Map the "id" column to id
                retrievedPlayer.setName(rs.getString("name")); // Map the "name" column to name
                playersWithSpecificName.add(retrievedPlayer);
            }
            return playersWithSpecificName;
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred during SQL operation: " + e.getMessage(), e);
        }
    }
}
