package com.football.Table_Football_Scorekeeper_API.Repositories;

import com.football.Table_Football_Scorekeeper_API.Entities.Player;
import org.springframework.stereotype.Repository;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PlayerRepositoryImpl implements PlayerRepository {

    private static final String CREATE_PLAYER_TABLE = """
        CREATE TABLE IF NOT EXISTS player (
        id SERIAL PRIMARY KEY,
        name VARCHAR(50) NOT NULL
        );
    """;

    public PlayerRepositoryImpl() {
        String url = "jdbc:postgresql://localhost:5432/table_football_db";
        try (var conn = DriverManager.getConnection(url)) {
            try (var statement = conn.createStatement()) {
                statement.execute(CREATE_PLAYER_TABLE); // Create table only if it doesn't already exist
            }
        } catch (SQLException e) {
            System.err.println("Error during table creation: " + e.getMessage());
        }
    }

    private java.sql.Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/table_football_db";  // connection to the PostgreSQL database
        String username = "football_admin";
        String password = "EvoconChamp";
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public Optional<Player> addPlayer(Player player) {
        String insertPlayerSQL = "INSERT INTO player (name) VALUES (?)";

        try (var conn = getConnection();
             var statement = conn.prepareStatement(insertPlayerSQL, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, player.getName());
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                var generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    player.setPlayerId(generatedKeys.getLong(1));
                    return Optional.of(player);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding player: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Player> getPlayer(Long id) {
        String selectPlayerSQL = "SELECT * FROM player WHERE id = ?";

        try (var conn = getConnection();
             var statement = conn.prepareStatement(selectPlayerSQL)) {

            statement.setLong(1, id); // Set the ID parameter in the query
            var resultSet = statement.executeQuery(); // Execute the query

            if (resultSet.next()) { // Check if a result is returned
                // Create a Player object from the result set
                Player player = new Player();
                player.setPlayerId(resultSet.getLong("playerId")); // Map the "id" column to playerId
                player.setName(resultSet.getString("name")); // Map the "name" column to name
                return Optional.of(player); // Return the Player wrapped in an Optional
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving player: " + e.getMessage());
        }
        return Optional.empty(); // Return empty Optional if no player is found or an error occurs
    }

    @Override
    public List<Player> getAllPlayers() {
        String selectPlayerSQL = "SELECT * FROM player";
        List<Player> allPlayers = new ArrayList<>();

        try (var conn = getConnection();
             var statement = conn.prepareStatement(selectPlayerSQL)) {

            var resultSet = statement.executeQuery(); // Execute the query

            while (resultSet.next()) { // Loop through all rows in the result set
                // Create a Player object from the current row
                Player player = new Player();
                player.setPlayerId(resultSet.getLong("playerId")); // Map the "id" column to playerId
                player.setName(resultSet.getString("name")); // Map the "name" column to name
                allPlayers.add(player); // Add the player to the list
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all players: " + e.getMessage());
        }
        return allPlayers;
    }

    @Override
    public Optional<Player> updatePlayer(Long id, Player player) {
        String updatePlayerSQL = "UPDATE player SET name = ? WHERE id = ?";
        String selectUpdatedPlayerSQL = "SELECT * FROM player WHERE id = ?";

        Player existingPlayer;

        try (var conn = getConnection();
             var updateStatement = conn.prepareStatement(updatePlayerSQL);
             var selectStatement = conn.prepareStatement(selectUpdatedPlayerSQL)) {

            // Set parameters for the UPDATE statement
            updateStatement.setString(1, player.getName());
            updateStatement.setLong(2, id);

            // Execute the UPDATE statement
            int rowsAffected = updateStatement.executeUpdate();

            // If no rows were updated, return an empty Optional
            if (rowsAffected == 0) {
                return Optional.empty();
            }

            // Fetch the updated player from the database
            selectStatement.setLong(1, id);
            var resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                Player updatedPlayer = new Player();
                updatedPlayer.setPlayerId(resultSet.getLong("playerId"));
                updatedPlayer.setName(resultSet.getString("name"));
                return Optional.of(updatedPlayer);
            }

        } catch (SQLException e) {
            System.err.println("Error updating player: " + e.getMessage());
        }
        return Optional.empty(); // Return empty if update fails
    }

    @Override
    public boolean deletePlayer(Long id) {
        String deletePlayerSQL = "UPDATE FROM player WHERE id = ?";
        boolean isDeleted = false;

        try (var conn = getConnection();
             var deleteStatement = conn.prepareStatement(deletePlayerSQL)) {

            deleteStatement.setLong(1, id); // Set parameters for the DELETE statement
            int rowsAffected = deleteStatement.executeUpdate(); // Execute the DELETE statement
            isDeleted = rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting player with " + id + ": " + e.getMessage());
        }
        return isDeleted;
    }

    @Override
    public List<Player> findByNameIgnoreCase(String name) {

        if (name == null || name.trim().isEmpty()) {
            return List.of(); // Return an empty list if the input is invalid
        }

        String selectPlayerSQL = "SELECT * FROM player WHERE name ILIKE ?";
        List<Player> playersWithSpecificName = new ArrayList<>();

        try (var conn = getConnection();
             var statement = conn.prepareStatement(selectPlayerSQL)) {

            statement.setString(1, name + "%");
            var resultSet = statement.executeQuery(); // Execute the query

            while (resultSet.next()) { // Loop through all rows in the result set
                // Create a Player object from the current row
                Player player = new Player();
                player.setPlayerId(resultSet.getLong("playerId")); // Map the "id" column to playerId
                player.setName(resultSet.getString("name")); // Map the "name" column to name
                playersWithSpecificName.add(player); // Add the player to the list
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all players with the name '" + name + "': " + e.getMessage());
        }
        return playersWithSpecificName;
    }
}
