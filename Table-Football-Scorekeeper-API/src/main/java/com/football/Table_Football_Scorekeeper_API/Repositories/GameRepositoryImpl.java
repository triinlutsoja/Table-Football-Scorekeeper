package com.football.Table_Football_Scorekeeper_API.Repositories;

import com.football.Table_Football_Scorekeeper_API.DatabaseConnection;
import com.football.Table_Football_Scorekeeper_API.Entities.Game;
import com.football.Table_Football_Scorekeeper_API.Entities.PlayerStat;
import com.football.Table_Football_Scorekeeper_API.Profile;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Repository
public class GameRepositoryImpl implements GameRepository {

    private final DatabaseConnection db;
    Properties props = Profile.getProperties("db");

    public GameRepositoryImpl() {
        // fetch a single instance of the DatabaseConnection class (Singleton Pattern)
        db = DatabaseConnection.instance(); // one AND only instance of the db connection
    }

    @Override
    public Game addGame(Game game) {

        // Get connection, execute action, add game to the database
        try (Connection conn = db.connect(props);
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

                // Query the database to fetch the just added game and return it
                PreparedStatement selectStmt = conn.prepareStatement("SELECT * FROM game WHERE id = ?");
                selectStmt.setLong(1, generatedId);
                ResultSet rs = selectStmt.executeQuery();
                if (rs.next()) {
                    Game retrievedGame = new Game();
                    retrievedGame.setId(rs.getLong("id"));
                    retrievedGame.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                    retrievedGame.setGreyId(rs.getLong("greyId"));
                    retrievedGame.setBlackId(rs.getLong("blackId"));
                    retrievedGame.setScoreGrey(rs.getInt("scoreGrey"));
                    retrievedGame.setScoreBlack(rs.getInt("scoreBlack"));
                    return retrievedGame;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred during SQL operation: " + e.getMessage(), e);
        }
        // If adding a new game fails, throw an exception
        throw new RuntimeException("Failed to add game to the database.");
    }


    @Override
    public Optional<Game> getGame(Long id) {

        // Get connection, execute query, get game from the database. Try-with-resources closes stuff automatically
        try (Connection conn = db.connect(props);  // fetching the existing connection from DatabaseConnection
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
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred during SQL operation: " + e.getMessage(), e);
        }
        return Optional.empty(); // Return empty Optional if no game with the given id is found
    }

    @Override
    public List<Game> getAllGames() {
        List<Game> games = new ArrayList<>();

        // Get connection, execute query, get all games from the database. Try-with-resources closes stuff automatically
        try (Connection conn = db.connect(props);  // fetching the existing connection from DatabaseConnection
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
            throw new RuntimeException("Error occurred during SQL operation: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Game> updateGame(Long id, Game game) {

        // Get connection, execute query, update. Try-with-resources closes stuff automatically
        try (Connection conn = db.connect(props)) {  // fetching the existing connection from DatabaseConnection

            // Check if the game exists
            try (PreparedStatement selectStmt = conn.prepareStatement("SELECT * FROM game WHERE id=?")) {
                selectStmt.setLong(1, id);
                ResultSet rs = selectStmt.executeQuery();
                if (!rs.next()) {
                    return Optional.empty();
                }
            }

            // Update the existing game
            try (PreparedStatement updateStmt =
                         conn.prepareStatement("UPDATE game SET greyId=?, blackId=?, scoreGrey=?,scoreBlack=? WHERE " +
                                 "id=?")) {
                updateStmt.setLong(1, game.getGreyId());
                updateStmt.setLong(2, game.getBlackId());
                updateStmt.setInt(3, game.getScoreGrey());
                updateStmt.setInt(4, game.getScoreBlack());
                updateStmt.setLong(5, id);
                updateStmt.executeUpdate();
            }

            // Retrieve updated game from the database
            try (PreparedStatement selectStmt = conn.prepareStatement("SELECT * FROM game WHERE id=?")) {
                selectStmt.setLong(1, id);
                ResultSet rs = selectStmt.executeQuery();

                if (rs.next()) {
                    Game updatedGame = new Game();
                    updatedGame.setId(rs.getLong("id"));
                    updatedGame.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                    updatedGame.setGreyId(rs.getLong("greyId"));
                    updatedGame.setBlackId(rs.getLong("blackId"));
                    updatedGame.setScoreGrey(rs.getInt("scoreGrey"));
                    updatedGame.setScoreBlack(rs.getInt("scoreBlack"));
                    return Optional.of(updatedGame);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred during SQL operation: " + e.getMessage(), e);
        }
        return Optional.empty();
    }


    @Override
    public boolean deleteGame(Long id) {

        // Get connection, execute query, delete. Try-with-resources closes stuff automatically
        try (Connection conn = db.connect(props)) {

            // Check if game exists
            try (PreparedStatement selectStmt = conn.prepareStatement("SELECT 1 FROM game WHERE id=?")) {
                selectStmt.setLong(1, id);
                ResultSet rs = selectStmt.executeQuery();
                if (!rs.next()) {
                    return false;  // If no such game exists, return false
                }
            }

            // Delete the existing game
            try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM game WHERE id=?")){
                deleteStmt.setLong(1, id);
                int rowsAffected = deleteStmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting player with " + id + ": " + e.getMessage());
            throw new RuntimeException("Error occurred during SQL operation: " + e.getMessage(), e);
        }
    }

    public List<PlayerStat> calculateWinStats() {
        List<PlayerStat> winnerStats = new ArrayList<>();

        // Get connection, execute query, get all game stats from the database. Try-with-resources closes stuff
        // automatically
        try (Connection conn = db.connect(props);  // fetching the existing connection from DatabaseConnection
             PreparedStatement selectStmt =
                     conn.prepareStatement("SELECT player.name AS playerName, COUNT(*) AS victories\n" +
                             "FROM player\n" +
                             "JOIN (\n" +
                             "    SELECT \n" +
                             "        CASE \n" +
                             "            WHEN scoreGrey > scoreBlack THEN greyId\n" +
                             "            WHEN scoreBlack > scoreGrey THEN blackId\n" +
                             "            ELSE NULL\n" +
                             "        END AS winnerId\n" +
                             "    FROM game\n" +
                             ") winners ON player.id = winners.winnerId\n" +
                             "GROUP BY player.name;")) {

            ResultSet rs = selectStmt.executeQuery();  // ResultSet contains both playerName and victories columns

            while (rs.next()) {
                // Create a PlayerStat object from the result set
                PlayerStat retrievedPlayerStat = new PlayerStat();
                retrievedPlayerStat.setPlayerName(rs.getString("playerName"));
                retrievedPlayerStat.setVictoryCount(rs.getInt("victories"));
                winnerStats.add(retrievedPlayerStat);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred during SQL operation: " + e.getMessage(), e);
        }
        return winnerStats; // Return the List of game stats (even if it's empty)
    }
}
