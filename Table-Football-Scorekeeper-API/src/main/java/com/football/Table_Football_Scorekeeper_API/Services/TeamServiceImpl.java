package com.football.Table_Football_Scorekeeper_API.Services;

import com.football.Table_Football_Scorekeeper_API.Entities.Team;
import com.football.Table_Football_Scorekeeper_API.Exceptions.EntityNotFoundException;
import com.football.Table_Football_Scorekeeper_API.Exceptions.ValidationException;
import com.football.Table_Football_Scorekeeper_API.Repositories.PlayerRepository;
import com.football.Table_Football_Scorekeeper_API.Repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;


    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, PlayerRepository playerRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    private boolean validateTeam(Team team) {

        // Players' IDs can't both be null
        if (team.getPlayer1Id() == null && team.getPlayer2Id() == null) {
            throw new ValidationException("TeamService: Players' IDs can't both be null, a team should have at least " +
                    "one player for a singles game and two players for doubles game.");
        }

        // In the case of a singles game, player1 can't be null
        if (team.getPlayer1Id() == null) {
            throw new ValidationException("TeamService: Player1 can never be null, a team should always have at least" +
                    " one " +
                    "player (singles game).");
        }

        //  Players' IDs must already exist in the player table
        if (team.getPlayer1Id() != null) {
            if (playerRepository.getPlayer(team.getPlayer1Id()).isEmpty()) {
                throw new ValidationException("TeamService: Player1 does not exist and was not found in the players table.");
            }
        }
        if (team.getPlayer2Id() != null) {
            if (playerRepository.getPlayer(team.getPlayer2Id()).isEmpty()) {
                throw new ValidationException("TeamService: Player2 does not exist and was not found in the players table.");
            }
        }

        // No self-pairing allowed in a doubles game
        if (team.getPlayer1Id() != null && team.getPlayer2Id() != null) {
            if (team.getPlayer1Id().equals(team.getPlayer2Id())) {
                throw new ValidationException("TeamService: No self-pairing allowed, the players in the team cannot be " +
                        "the same player.");
            }
        }

        // Don't allow creating duplicate teams (even if player1 and player2 are reversed)
        List<Team> teams = teamRepository.getAllTeams();

        // Check for singles game
        if (team.getPlayer2Id() == null) {
            for (Team t : teams) {
                System.out.println("Comparing against team: " + t.getPlayer1Id() + " & " + t.getPlayer2Id());
                System.out.println("New team: " + team.getPlayer1Id() + " & " + team.getPlayer2Id());
                if (Objects.equals(t.getPlayer1Id(), team.getPlayer1Id()) && t.getPlayer2Id() == null) {
                    System.out.println("Match found");
                    throw new ValidationException("TeamService: Singles team with this player already exists.");
                }
            }
        }

        // Check for doubles game
        if (team.getPlayer1Id() != null && team.getPlayer2Id() != null) {
            for (Team t : teams) {
                if ((Objects.equals(t.getPlayer1Id(), team.getPlayer1Id()) || Objects.equals(t.getPlayer1Id(),
                        team.getPlayer2Id())) && (Objects.equals(t.getPlayer2Id(), team.getPlayer1Id()) || Objects.equals(t.getPlayer2Id(),
                        team.getPlayer2Id()))) {
                    throw new ValidationException("TeamService: Team already exists with these specific players.");
                }
            }
        }

        return true;
    }

    @Override
    public Team addTeam(Team team) {
        // Validate the player object, will throw ValidationException if invalid
        validateTeam(team);

        // If validation passes, delegate the operation to the repository
        return teamRepository.addTeam(team);
    }

    @Override
    public Optional<Team> getTeam(Long id) {
        Optional<Team> retrievedTeam = teamRepository.getTeam(id);
        if (retrievedTeam.isPresent()) {
            return teamRepository.getTeam(id);
        } else {
            throw new EntityNotFoundException("TeamService: Team with id " + id + " not found.");
        }
    }

    @Override
    public List<Team> getAllTeams() {
        return teamRepository.getAllTeams();
    }

    @Override
    public Team updateTeam(Long id, Team team) {
        // Validate the player object, will throw ValidationException if invalid
        validateTeam(team);

        if (getTeam(id).isPresent()) {
            return teamRepository.updateTeam(id, team);
        } else {
            throw new EntityNotFoundException("TeamService: Team with id " + id + " not found.");
        }
    }

    @Override
    public boolean deleteTeam(Long id) {
        if (teamRepository.deleteTeam(id)) {
            return true;
        } else {
            throw new EntityNotFoundException("TeamService: Team with id " + id + " not found.");
        }
    }
}
