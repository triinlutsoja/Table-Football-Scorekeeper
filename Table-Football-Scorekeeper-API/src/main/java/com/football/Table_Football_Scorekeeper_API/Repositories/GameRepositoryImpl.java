package com.football.Table_Football_Scorekeeper_API.Repositories;

import com.football.Table_Football_Scorekeeper_API.DatabaseConnection;
import com.football.Table_Football_Scorekeeper_API.Entities.Game;
import com.football.Table_Football_Scorekeeper_API.Entities.Player;
import com.football.Table_Football_Scorekeeper_API.Profile;
import com.football.Table_Football_Scorekeeper_API.TableFootballScorekeeperApiApplication;
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

    static {
        try {
            // Load JDBC class for MySQL driver. (not always required since JDBC 4.0+ automatically loads drivers)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found", e);
        }
    }

    public GameRepositoryImpl() {
        // fetch a single instance of the DatabaseConnection class (Singleton Pattern)
        db = DatabaseConnection.instance(); // one AND only instance of the db connection
    }

    @Override
    public Game addGame(Game game) {

        Properties props = Profile.getProperties("db");

        // try to establish connection
        try {
            db.connect(props);
            System.out.println("Connected.");  // TODO: remove printing to console.
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database: " + e.getMessage(), e);
        }

        // Get connection, execute action, add game to the database
        try (Connection conn = db.getConnection();
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
            insertStmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred during SQL operation: " + e.getMessage(), e);
        }
        // If adding a new game fails, throw an exception
        throw new RuntimeException("Failed to add game to the database.");
    }


    @Override
    public Optional<Game> getGame(Long id) {
        Properties props = Profile.getProperties("db");

        // try to establish connection
        try {
            db.connect(props);
            System.out.println("Connected.");  // TODO: remove printing to console.
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database: " + e.getMessage(), e);
        }
        // Get connection, execute query, get game from the database. Try-with-resources closes stuff automatically
        try (Connection conn = db.getConnection();  // fetching the existing connection from DatabaseConnection
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
        Properties props = Profile.getProperties("db");
        List<Game> games = new ArrayList<>();

        // try to establish connection
        try {
            db.connect(props);
            System.out.println("Connected.");  // TODO: remove printing to console.
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database: " + e.getMessage(), e);
        }
        // Get connection, execute query, get all games from the database. Try-with-resources closes stuff automatically
        try (Connection conn = db.getConnection();  // fetching the existing connection from DatabaseConnection
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

    /* COMMENTING OUT FOR NOW

    @Override
    public Optional<Game> updateGame(Long id, Game game) {
        return Optional.empty();
    }

    @Override
    public boolean deleteGame(Long id) {
        return false;
    }*/
}
