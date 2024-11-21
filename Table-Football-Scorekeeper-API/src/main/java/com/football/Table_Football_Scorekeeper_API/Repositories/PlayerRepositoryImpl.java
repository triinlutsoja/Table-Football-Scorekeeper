package com.football.Table_Football_Scorekeeper_API.Repositories;

import com.football.Table_Football_Scorekeeper_API.Entities.Player;
import org.springframework.stereotype.Repository;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class PlayerRepositoryImpl implements PlayerRepository {

    // TODO: create table – where and when?

    @Override
    public Optional<Player> addPlayer(Player player) {
        String url = "jdbc:postgresql://localhost:5432/table_football_db";  // connection to the PostgreSQL database
        try (var conn = DriverManager.getConnection(url)) {  // opening a connection with the database
            if (conn != null) {  // checks if connection has been opened successfully
                var statement = conn.prepareStatement("INSERT INTO player (name) VALUES (?)",  // prepares SQL statement
                        java.sql.Statement.RETURN_GENERATED_KEYS); // returns the auto-generated key created during the
                // execution of this INSERT statement.
                statement.setString(1, player.getName()); // sets player's name in the prepared statement
                int rowsAffected = statement.executeUpdate();  // running the prepared statement

                if (rowsAffected > 0) {  // if greater than 1, then adding player was successful
                    var generatedKeys = statement.getGeneratedKeys();  // retrieves the generated IDs
                    if (generatedKeys.next()) {
                        player.setPlayerId(generatedKeys.getLong(1));  // Set the generated ID to the player object
                        return Optional.of(player);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting player: " + e.getMessage());
        }

        // When the try block is exited, Java automatically calls the close() method on conn (the database connection).

        return Optional.empty();  // return empty if insertion fails
    }

    @Override
    public Optional<Player> getPlayer(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Player> getAllPlayers() {
        return List.of();
    }

    @Override
    public Optional<Player> updatePlayer(Long id, Player player) {
        return Optional.empty();
    }

    @Override
    public boolean deletePlayer(Long id) {
        return false;
    }

    @Override
    public List<Player> findByNameIgnoreCase(String name) {
        return List.of();
    }
}
