package com.football.Table_Football_Scorekeeper_API.Repositories;

import com.football.Table_Football_Scorekeeper_API.Entities.Player;
import com.football.Table_Football_Scorekeeper_API.DatabaseConnection;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

@Repository
public class PlayerRepositoryImpl implements PlayerRepository {

    private final DatabaseConnection db;

    static {
        try {
            // Load JDBC class for MySQL driver. (not always required since JDBC 4.0+ automatically loads drivers)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found", e);
        }
    }

    public PlayerRepositoryImpl() {
        // create a single instance of the DatabaseConnection class (Singleton Pattern)
        db = DatabaseConnection.instance(); // one AND only instance of the db connection
    }

    @Override
    public Player addPlayer(Player player) {
        Connection conn = null;

        // try to establish connection
        try {
            db.connect();
            System.out.println("Connected.");  // TODO: remove printing to console.
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database: " + e.getMessage(), e);
        }

        try {
            // Get connection, execute action, add player to the database
            conn = db.getConnection();  // fetching the existing connection from DatabaseConnection
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO player (name) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, player.getName());
            stmt.executeUpdate();

            // Retrieve the auto-generated ID
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                Long generatedId = generatedKeys.getLong(1);  // The first column is the generated key

                // Query the database to fetch the just added player and return it
                stmt = conn.prepareStatement("SELECT id, name FROM player WHERE id = ?");
                stmt.setLong(1, generatedId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    Player retrievedPlayer = new Player();
                    Long retrievedId = rs.getLong("id");
                    String retrievedName = rs.getString("name");
                    retrievedPlayer.setId(retrievedId);
                    retrievedPlayer.setName(retrievedName);
                    return retrievedPlayer;
                }
            }
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred during SQL operation: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                // try to close connection
                try {
                    db.close();
                    System.out.println("Disconnected.");
                } catch (SQLException e) {
                    System.out.println("Cannot close the database connection.");
                }
            }
        }
        // If adding a new player fails, throw an exception
        throw new RuntimeException("Failed to add player to the database.");
    }

    @Override
    public Optional<Player> getPlayer(Long id) {
        // try to establish connection
        try {
            db.connect();
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

    /* COMMENTING OUT

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
                player.setId(resultSet.getLong("Id")); // Map the "id" column to id
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
                updatedPlayer.setId(resultSet.getLong("id"));
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
                player.setId(resultSet.getLong("id")); // Map the "id" column to id
                player.setName(resultSet.getString("name")); // Map the "name" column to name
                playersWithSpecificName.add(player); // Add the player to the list
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all players with the name '" + name + "': " + e.getMessage());
        }
        return playersWithSpecificName;
    }*/
}
