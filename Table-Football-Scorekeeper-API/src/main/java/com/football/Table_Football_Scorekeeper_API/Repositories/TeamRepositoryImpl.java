package com.football.Table_Football_Scorekeeper_API.Repositories;

import com.football.Table_Football_Scorekeeper_API.DatabaseConnection;
import com.football.Table_Football_Scorekeeper_API.Entities.Team;
import com.football.Table_Football_Scorekeeper_API.Exceptions.DatabaseException;
import com.football.Table_Football_Scorekeeper_API.Profile;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Repository
public class TeamRepositoryImpl implements TeamRepository {

    private final DatabaseConnection db;
    Properties props = Profile.getProperties("db");

    public TeamRepositoryImpl(){
        // fetch a single instance of the DatabaseConnection class (Singleton Pattern)
        db = DatabaseConnection.instance(); // one AND only instance of the db connection
    }

    @Override
    public Team addTeam(Team team) {

        // Get connection, execute action, add team to the database
        try (Connection conn = db.connect(props);
             PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO team (player1Id, player2Id) VALUES(?,?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {

            insertStmt.setLong(1,team.getPlayer1Id());
            if (team.getPlayer2Id() != null) {
                insertStmt.setLong(2, team.getPlayer2Id());
            } else {
                insertStmt.setNull(2, Types.BIGINT);
            }
            insertStmt.executeUpdate();

            // Retrieve the auto-generated ID
            ResultSet generatedKeys = insertStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                Long generatedId = generatedKeys.getLong(1);
                team.setId(generatedId);
            } else {
                throw new DatabaseException("Failed to retrieve generated ID for the inserted team.");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during SQL operation: " + e.getMessage(), e);
        }
        return team;
    }

    @Override
    public Optional<Team> getTeam(Long id) {

        // Get connection, execute action, get team from the database
        try (Connection conn = db.connect(props);
             PreparedStatement selectStmt = conn.prepareStatement("SELECT * FROM team WHERE id=?")) {

            selectStmt.setLong(1, id);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                Team retrievedTeam = new Team();
                retrievedTeam.setId(rs.getLong("id"));
                retrievedTeam.setPlayer1Id(rs.getLong("player1Id"));
                retrievedTeam.setPlayer2Id(rs.getLong("player2Id"));
                return Optional.of(retrievedTeam);
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during SQL operation: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Team> getAllTeams() {
        List<Team> teams = new ArrayList<>();

        // Get connection, execute action, get all teams from the database
        try (Connection conn = db.connect(props);
             PreparedStatement selectStmt = conn.prepareStatement("SELECT * FROM team")) {

            ResultSet rs = selectStmt.executeQuery();

            while (rs.next()) {
                Team retrievedTeam = new Team();
                retrievedTeam.setId(rs.getLong("id"));
                retrievedTeam.setPlayer1Id(rs.getLong("player1Id"));
                retrievedTeam.setPlayer2Id(rs.getObject("player2Id", Long.class));  // player2Id is nullable in DB, so use getObject
                teams.add(retrievedTeam);
            }

            return teams;

        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during SQL operation: " + e.getMessage(), e);
        }
    }

    @Override
    public Team updateTeam(Long id, Team team) {

        // Get connection, execute action, update team
        try (Connection conn = db.connect(props);
             PreparedStatement updateStmt = conn.prepareStatement("UPDATE team SET player1Id=?, player2Id=? WHERE id=?",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {

            updateStmt.setLong(1,team.getPlayer1Id());
            if (team.getPlayer2Id() != null) {
                updateStmt.setLong(2, team.getPlayer2Id());
            } else {
                updateStmt.setNull(2, Types.BIGINT);
            }
            updateStmt.setLong(3, id);
            updateStmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during SQL operation: " + e.getMessage(), e);
        }

        team.setId(id);
        return team;
    }

    @Override
    public boolean deleteTeam(Long id) {

        // Get connection, execute action, add game to the database
        try (Connection conn = db.connect(props);
             PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM team WHERE id=?",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {

            deleteStmt.setLong(1, id);
            int rowsAffected = deleteStmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during SQL operation: " + e.getMessage(), e);
        }
    }
}
