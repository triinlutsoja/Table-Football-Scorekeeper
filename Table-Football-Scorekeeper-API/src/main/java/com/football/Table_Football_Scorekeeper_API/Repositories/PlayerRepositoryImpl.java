package com.football.Table_Football_Scorekeeper_API.Repositories;

import com.football.Table_Football_Scorekeeper_API.Entities.Player;
import com.football.Table_Football_Scorekeeper_API.DatabaseConnection;
import com.football.Table_Football_Scorekeeper_API.Exceptions.DatabaseException;
import com.football.Table_Football_Scorekeeper_API.Profile;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Repository
public class PlayerRepositoryImpl implements PlayerRepository {

    private final DatabaseConnection db;
    Properties props = Profile.getProperties("db");

    public PlayerRepositoryImpl() {
        // fetch a single instance of the DatabaseConnection class (Singleton Pattern)
        db = DatabaseConnection.instance(); // one AND only instance of the db connection
    }

    @Override
    public Player addPlayer(Player player) {
        Long generatedId = null;
        // Get connection, execute action, add player to the database
        try (Connection conn = db.connect(props);  // fetching the existing connection from DatabaseConnection
             PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO player (name) VALUES(?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            insertStmt.setString(1, player.getName());
            insertStmt.executeUpdate();

            // Retrieve the auto-generated ID
            ResultSet generatedKeys = insertStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getLong(1);  // The first column is the generated key
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during SQL operation: " + e.getMessage(), e);
        }

        // Set the generated ID on the player object
        player.setId(generatedId);
        return player;
    }

    @Override
    public Optional<Player> getPlayer(Long id) {

        // Get connection, execute query, get player from the database. Try-with-resources closes stuff automatically
        try (Connection conn = db.connect(props);  // fetching the existing connection from DatabaseConnection
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
            return Optional.empty(); // Return empty Optional if no player with the given id is found

        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during SQL operation: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Player> getAllPlayers() {
        List<Player> allPlayers = new ArrayList<>();

        // Get connection, execute query, get player from the database. Try-with-resources closes stuff automatically
        try (Connection conn = db.connect(props);  // fetching the existing connection from DatabaseConnection
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
            throw new DatabaseException("Error occurred during SQL operation: " + e.getMessage(), e);
        }
    }

    @Override
    public Player updatePlayer(Long id, Player player) {

        try (Connection conn = db.connect(props);
             PreparedStatement updateStmt = conn.prepareStatement("UPDATE player SET name = ? WHERE id = ?")) {  //
            // fetching the existing connection from DatabaseConnection

            // Attempt to update the player
            updateStmt.setString(1, player.getName());
            updateStmt.setLong(2, id);
            updateStmt.executeUpdate();

        } catch (SQLException e) {  // Letting SQLException propagate up to the controller layer is generally discouraged because it’s a low-level, database-specific detail.
            throw new DatabaseException("Error updating player with id " + id, e);  // It’s better to wrap it in a
            // custom exception  and propagate.
        }

        // Return the updated player
        player.setId(id);
        return player;
    }

    @Override
    public boolean deletePlayer(Long id) {

        try (Connection conn = db.connect(props)) {  // fetching the existing connection from DatabaseConnection
            // Delete the existing player
            try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM player WHERE id = ?")) {

                deleteStmt.setLong(1, id); // Set parameters for the DELETE statement
                int rowsAffected = deleteStmt.executeUpdate(); // Execute the DELETE statement
                return rowsAffected > 0;  // if returns true, deletion succeeded. If false, the id didn't exist.
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during SQL operation: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Player> findByNameIgnoreCase(String name) {
        List<Player> playersWithSpecificName = new ArrayList<>();

        // Get connection, execute query, get player from the database. Try-with-resources closes stuff automatically
        try (Connection conn = db.connect(props);  // fetching the existing connection from DatabaseConnection
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
            throw new DatabaseException("Error occurred during SQL operation: " + e.getMessage(), e);
        }
    }
}
