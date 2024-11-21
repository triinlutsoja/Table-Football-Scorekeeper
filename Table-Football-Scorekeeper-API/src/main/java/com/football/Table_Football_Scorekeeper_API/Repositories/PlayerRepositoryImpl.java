package com.football.Table_Football_Scorekeeper_API.Repositories;

import com.football.Table_Football_Scorekeeper_API.Entities.Player;
import org.springframework.stereotype.Repository;

import java.sql.DriverManager;
import java.sql.SQLException;
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
