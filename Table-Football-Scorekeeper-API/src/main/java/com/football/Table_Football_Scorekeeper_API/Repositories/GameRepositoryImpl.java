package com.football.Table_Football_Scorekeeper_API.Repositories;

import com.football.Table_Football_Scorekeeper_API.DatabaseConnection;
import com.football.Table_Football_Scorekeeper_API.Entities.Game;
import com.football.Table_Football_Scorekeeper_API.Entities.PlayerStat;
import com.football.Table_Football_Scorekeeper_API.Exceptions.DatabaseException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class GameRepositoryImpl implements GameRepository {

    private final DataSource dataSource;

    public GameRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Game addGame(Game game) {

        // Get connection, execute action, add game to the database
        try (Connection conn = dataSource.getConnection();
             PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO game (greyId, blackId, scoreGrey, " +
                        "scoreBlack) VALUES(?,?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {

            insertStmt.setLong(1,game.getGreyId());
            insertStmt.setLong(2,game.getBlackId());
            insertStmt.setInt(3,game.getScoreGrey());
            insertStmt.setInt(4,game.getScoreBlack());
            insertStmt.executeUpdate();

            // Retrieve the auto-generated ID
            ResultSet generatedKeys = insertStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                Long generatedId = generatedKeys.getLong(1);
                game.setId(generatedId);
            } else {
                throw new DatabaseException("Failed to retrieve generated ID for the inserted game.");
            }

            // Perform a SELECT query to retrieve the timestamp
            try (PreparedStatement selectStmt = conn.prepareStatement("SELECT timestamp FROM game WHERE id = ?")) {
                selectStmt.setLong(1, game.getId());
                ResultSet rs = selectStmt.executeQuery();
                if (rs.next()) {
                    game.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                } else {
                    throw new DatabaseException("Failed to retrieve timestamp for the inserted game.");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during SQL operation: " + e.getMessage(), e);
        }
        return game;
    }


    @Override
    public Optional<Game> getGame(Long id) {

        // Get connection, execute query, get game from the database. Try-with-resources closes stuff automatically
        try (Connection conn = dataSource.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement("SELECT * FROM game WHERE id = ?")) {

            selectStmt.setLong(1, id); // Set the ID parameter in the query
            ResultSet rs = selectStmt.executeQuery(); // Execute the query

            if (rs.next()) { // Check if a result is returned
                // Create a Game object from the result set
                Game retrievedGame = new Game();
                retrievedGame.setId(rs.getLong("id")); // Map the "id" column to id
                retrievedGame.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime()); // Map the "timestamp"
                // column to timestamp
                retrievedGame.setGreyId(rs.getLong("greyId"));
                retrievedGame.setBlackId(rs.getLong("blackId"));
                retrievedGame.setScoreGrey(rs.getInt("scoreGrey"));
                retrievedGame.setScoreBlack(rs.getInt("scoreBlack"));
                return Optional.of(retrievedGame); // Return the Game wrapped in an Optional
            }
            return Optional.empty(); // Return empty Optional if no game with the given id is found
        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during SQL operation: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Game> getAllGames() {
        List<Game> games = new ArrayList<>();

        // Get connection, execute query, get all games from the database. Try-with-resources closes stuff automatically
        try (Connection conn = dataSource.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement("SELECT * FROM game")) {

            ResultSet rs = selectStmt.executeQuery();

            while (rs.next()) {
                // Create a Game object from the result set
                Game retrievedGame = new Game();
                retrievedGame.setId(rs.getLong("id"));
                retrievedGame.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                retrievedGame.setGreyId(rs.getLong("greyId"));
                retrievedGame.setBlackId(rs.getLong("blackId"));
                retrievedGame.setScoreGrey(rs.getInt("scoreGrey"));
                retrievedGame.setScoreBlack(rs.getInt("scoreBlack"));
                games.add(retrievedGame);
            }
            return games; // Return the List of games (even if it's empty)
        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during SQL operation: " + e.getMessage(), e);
        }
    }

    @Override
    public Game updateGame(Long id, Game game) {

        // Get connection, execute query, update. Try-with-resources closes stuff automatically
        try (Connection conn = dataSource.getConnection()) {

            // Attempt to update the game
            try (PreparedStatement updateStmt =
                         conn.prepareStatement("UPDATE game SET greyId=?, blackId=?, scoreGrey=?,scoreBlack=?," +
                                 "timestamp=? WHERE id=?")) {
                updateStmt.setLong(1, game.getGreyId());
                updateStmt.setLong(2, game.getBlackId());
                updateStmt.setInt(3, game.getScoreGrey());
                updateStmt.setInt(4, game.getScoreBlack());
                updateStmt.setTimestamp(5, Timestamp.valueOf(game.getTimestamp()));
                updateStmt.setLong(6, id);
                updateStmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during SQL operation: " + e.getMessage(), e);
        }

        // Return the updated game
        game.setId(id);
        return game;
    }


    @Override
    public boolean deleteGame(Long id) {

        // Get connection, execute query, delete. Try-with-resources closes stuff automatically
        try (Connection conn = dataSource.getConnection()) {

            // Delete the existing game
            try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM game WHERE id=?")){
                deleteStmt.setLong(1, id);
                int rowsAffected = deleteStmt.executeUpdate();
                return rowsAffected > 0;  // If true, game exists and has been deleted. If false, didn't exist
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during SQL operation: " + e.getMessage(), e);
        }
    }

    public List<PlayerStat> calculateWinStats() {
        List<PlayerStat> winnerStats = new ArrayList<>();

        // Get connection, execute query, get all game stats from the database. Try-with-resources closes stuff
        // automatically
        try (Connection conn = dataSource.getConnection();
             PreparedStatement selectStmt =
                     conn.prepareStatement("SELECT player.name AS playerName, COUNT(*) AS victoryCount\n" +
                             "FROM game\n" +
                             "JOIN team ON (\n" +
                             "    (game.scoreGrey = 8 AND game.greyId = team.id)\n" +
                             "    OR\n" +
                             "    (game.scoreBlack = 8 AND game.blackId = team.id)\n" +
                             ")\n" +
                             "JOIN player ON (\n" +
                             "    player.id = team.player1Id OR player.id = team.player2Id\n" +
                             ")\n" +
                             "GROUP BY player.id, player.name\n" +
                             "ORDER BY victoryCount DESC;")) {

            ResultSet rs = selectStmt.executeQuery();  // ResultSet contains both playerName and victories columns

            while (rs.next()) {
                // Create a PlayerStat object from the result set
                PlayerStat retrievedPlayerStat = new PlayerStat();
                retrievedPlayerStat.setPlayerName(rs.getString("playerName"));
                retrievedPlayerStat.setVictoryCount(rs.getInt("victoryCount"));
                winnerStats.add(retrievedPlayerStat);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during SQL operation: " + e.getMessage(), e);
        }
        return winnerStats; // Return the List of game stats (even if it's empty)
    }
}
