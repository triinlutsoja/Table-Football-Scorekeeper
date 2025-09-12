package com.football.Table_Football_Scorekeeper_API.Services;

import com.football.Table_Football_Scorekeeper_API.Entities.Player;
import com.football.Table_Football_Scorekeeper_API.Entities.Team;
import com.football.Table_Football_Scorekeeper_API.Exceptions.EntityNotFoundException;
import com.football.Table_Football_Scorekeeper_API.Exceptions.ValidationException;
import com.football.Table_Football_Scorekeeper_API.Repositories.PlayerRepository;
import com.football.Table_Football_Scorekeeper_API.Repositories.TeamRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TeamServiceImplTest {

    private static final Logger log = LoggerFactory.getLogger(TeamServiceImplTest.class);
    private TeamServiceImpl teamService;
    private TeamRepository teamRepository;
    private PlayerRepository playerRepository;
    
    @BeforeEach
    void setUp() {
        teamRepository = Mockito.mock(TeamRepository.class);
        playerRepository = Mockito.mock(PlayerRepository.class);
        teamService = new TeamServiceImpl(teamRepository, playerRepository);
    }

    @Test
    void addTeam_ShouldSaveTeam_WhenDoublesGame() {
        //Arrange
        Player existingPlayer1 = new Player("John Doe");
        existingPlayer1.setId(1L);
        Player existingPlayer2 = new Player("Jane Doe");
        existingPlayer2.setId(2L);

        Team team = new Team(existingPlayer1.getId(), existingPlayer2.getId());
        Team savedTeam = new Team(existingPlayer1.getId(), existingPlayer2.getId());
        savedTeam.setId(100L);

        when(teamRepository.addTeam(team)).thenReturn(savedTeam);
        when(playerRepository.getPlayer(team.getPlayer1Id())).thenReturn(Optional.of(existingPlayer1));
        when(playerRepository.getPlayer(team.getPlayer2Id())).thenReturn(Optional.of(existingPlayer2));

        // Act
        Team result = teamService.addTeam(team);

        // Assert
        Assertions.assertNotNull(result, "The saved team should not be null");
        Assertions.assertEquals(1L, result.getPlayer1Id());
        Assertions.assertEquals(2L, result.getPlayer2Id());
        verify(teamRepository, times(1)).addTeam(team);
    }

    @Test
    void addTeam_ShouldSaveTeam_WhenSinglesTeam() {
        //Arrange
        Player existingPlayer1 = new Player("John Doe");
        existingPlayer1.setId(1L);

        Team team = new Team(1L, null);
        Team savedTeam = new Team(1L, null);
        savedTeam.setId(100L);

        when(teamRepository.addTeam(team)).thenReturn(savedTeam);
        when(playerRepository.getPlayer(team.getPlayer1Id())).thenReturn(Optional.of(existingPlayer1));
        when(playerRepository.getPlayer(team.getPlayer2Id())).thenReturn(Optional.empty());

        // Act
        Team result = teamService.addTeam(team);

        // Assert
        Assertions.assertNotNull(result, "The saved team should not be null.");
        Assertions.assertEquals(1L, result.getPlayer1Id());
        Assertions.assertNull(result.getPlayer2Id(), "Player2 should be null in case of a singles team.");
        verify(teamRepository, times(1)).addTeam(team);
    }

    @Test
    void addTeam_ShouldThrowValidationException_WhenBothPlayersIdsAreNull() {
        //Arrange
        Team invalidTeam = new Team(null, null);

        // Act + Assert
        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> teamService.addTeam(invalidTeam),
                "Expected ValidationException but none was thrown."
        );
        assertEquals("TeamService: Players' IDs can't both be null, a team should have at least " +
                "one player for a singles game and two players for doubles game.", thrown.getMessage());
    }

    @Test
    void addTeam_ShouldThrowValidationException_WhenPlayer1IsNull() {
        //Arrange
        Player existingPlayer2 = new Player("Jane Doe");
        existingPlayer2.setId(2L);

        Team invalidTeam = new Team(null, existingPlayer2.getId());

        // Act + Assert
        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> teamService.addTeam(invalidTeam),
                "Expected ValidationException but none was thrown."
        );
        assertEquals("TeamService: Player1 can never be null, a team should always have at least one player (singles game).", thrown.getMessage());
    }

    @Test
    void addTeam_ShouldThrowValidationException_WhenPlayer1DoesNotExist() {
        // Arrange
        Player existingPlayer = new Player("John Doe");
        existingPlayer.setId(1L);

        Player nonExistingPlayer = new Player("Jane Doe");
        nonExistingPlayer.setId(3L);

        Team invalidTeam = new Team(nonExistingPlayer.getId(), existingPlayer.getId());

        when(playerRepository.getPlayer(existingPlayer.getId())).thenReturn(Optional.of(existingPlayer));
        when(playerRepository.getPlayer(nonExistingPlayer.getId())).thenReturn(Optional.empty());

        // Act + Assert
        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> teamService.addTeam(invalidTeam),
                "Expected ValidationException but none was thrown."
        );
        assertEquals("TeamService: Player1 does not exist and was not found in the players table.", thrown.getMessage());
    }

    @Test
    void addTeam_ShouldThrowValidationException_WhenPlayer2DoesNotExist() {
        // Arrange
        Player existingPlayer = new Player("John Doe");
        existingPlayer.setId(1L);

        Player nonExistingPlayer = new Player("Jane Doe");
        nonExistingPlayer.setId(3L);

        Team invalidTeam = new Team(existingPlayer.getId(), nonExistingPlayer.getId());

        when(playerRepository.getPlayer(existingPlayer.getId())).thenReturn(Optional.of(existingPlayer));
        when(playerRepository.getPlayer(nonExistingPlayer.getId())).thenReturn(Optional.empty());

        // Act + Assert
        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> teamService.addTeam(invalidTeam),
                "Expected ValidationException but none was thrown."
        );
        assertEquals("TeamService: Player2 does not exist and was not found in the players table.",
                thrown.getMessage());
    }

    @Test
    void addTeam_ShouldThrowValidationException_WhenAttemptToSelfPairInDoublesGame() {
        // Arrange
        Player existingPlayer = new Player("John Doe");
        existingPlayer.setId(1L);

        Team invalidTeam = new Team(existingPlayer.getId(), existingPlayer.getId());

        when(playerRepository.getPlayer(existingPlayer.getId())).thenReturn(Optional.of(existingPlayer));

        // Act + Assert
        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> teamService.addTeam(invalidTeam),
                "Expected ValidationException but none was thrown."
        );
        assertEquals("TeamService: No self-pairing allowed, the players in the team cannot be the same player.",
                thrown.getMessage());
    }

    @Test
    void addTeam_ShouldThrowValidationException_WhenAttemptToSaveDuplicateSinglesTeam() {
        // Arrange
        Player existingPlayer1 = new Player("John Doe");
        existingPlayer1.setId(1L);

        List<Team> teams = new ArrayList<>();
        Team existingTeam = new Team(existingPlayer1.getId(), null);
        teams.add(existingTeam);
        Team invalidTeam = new Team(existingPlayer1.getId(), null);

        when(playerRepository.getPlayer(existingPlayer1.getId())).thenReturn(Optional.of(existingPlayer1));
        when(teamRepository.getAllTeams()).thenReturn(teams);

        // Act + Assert
        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> teamService.addTeam(invalidTeam),
                "Expected ValidationException but none was thrown."
        );
        assertEquals("TeamService: Singles team with this player already exists.",
                thrown.getMessage());
    }

    @Test
    void addTeam_ShouldThrowValidationException_WhenAttemptToSaveDuplicateDoublesTeam() {
        // Arrange
        Player existingPlayer1 = new Player("John Doe");
        existingPlayer1.setId(1L);

        Player existingPlayer2 = new Player("Jane Doe");
        existingPlayer2.setId(2L);

        List<Team> teams = new ArrayList<>();
        Team existingTeam = new Team(existingPlayer1.getId(), existingPlayer2.getId());
        teams.add(existingTeam);
        Team invalidTeam = new Team(existingPlayer1.getId(), existingPlayer2.getId());

        when(playerRepository.getPlayer(existingPlayer1.getId())).thenReturn(Optional.of(existingPlayer1));
        when(playerRepository.getPlayer(existingPlayer2.getId())).thenReturn(Optional.of(existingPlayer2));
        when(teamRepository.getAllTeams()).thenReturn(teams);

        // Act + Assert
        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> teamService.addTeam(invalidTeam),
                "Expected ValidationException but none was thrown."
        );
        assertEquals("TeamService: Team already exists with these specific players.",
                thrown.getMessage());
    }

    @Test
    void getTeam_ShouldReturnTeamById_WhenTeamExists() {
        // Arrange
        Team existingTeam = new Team();
        existingTeam.setId(1L);

        when(teamRepository.getTeam(existingTeam.getId())).thenReturn(Optional.of(existingTeam));

        // Act
        Optional<Team> result = teamRepository.getTeam(existingTeam.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(existingTeam, result.get());
    }

    @Test
    void getTeam_ShouldThrowEntityNotFoundException_WhenTeamDoesNotExist() {
        // Arrange
        Team nonExistingTeam = new Team();
        nonExistingTeam.setId(100L);

        when(teamRepository.getTeam(nonExistingTeam.getId())).thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () ->  teamService.getTeam(nonExistingTeam.getId()),
                "Expected EntityNotFoundException but none was thrown."
        );
        assertEquals("TeamService: Team with id " + nonExistingTeam.getId() + " not found.",
                thrown.getMessage());
    }

    @Test
    void getAllTeams_ShouldReturnAllTeams() {
        // Arrange
        List<Team> teams = new ArrayList<>();

        Team team1 = new Team();
        team1.setId(100L);
        teams.add(team1);

        Team team2 = new Team();
        team2.setId(101L);
        teams.add(team2);

        when(teamRepository.getAllTeams()).thenReturn(teams);

        // Act
        List<Team> result = teamService.getAllTeams();

        // Assert
        assertEquals(2, result.size());
        assertEquals(100L, result.get(0).getId());
        assertEquals(101L, result.get(1).getId());
    }

    @Test
    void updateTeam_ShouldUpdateTeam_WhenTeamExists() {

        // Arrange
        Player player1 = new Player("John Doe");
        player1.setId(1L);

        Player player2 = new Player("Jane Doe");
        player2.setId(3L);

        List<Team> teams = new ArrayList<>();

        Team existingTeam = new Team(player1.getId(), 2L);
        existingTeam.setId(1L);
        teams.add(existingTeam);

        Team updatedTeam = new Team(player1.getId(), player2.getId());
        updatedTeam.setId(1L);

        when(playerRepository.getPlayer(updatedTeam.getPlayer1Id())).thenReturn(Optional.of(player1));
        when(playerRepository.getPlayer(updatedTeam.getPlayer2Id())).thenReturn(Optional.of(player2));
        when(teamRepository.getAllTeams()).thenReturn(teams);
        when(teamRepository.updateTeam(existingTeam.getId(), updatedTeam)).thenReturn(updatedTeam);
        when(teamRepository.getTeam(existingTeam.getId())).thenReturn(Optional.of(existingTeam));

        // Act
        Team result = teamService.updateTeam(existingTeam.getId(), updatedTeam);

        // Assert
        assertNotNull(result);
        assertEquals(existingTeam.getId(), result.getId());
        assertEquals(updatedTeam.getPlayer1Id(), result.getPlayer1Id());
        assertEquals(updatedTeam.getPlayer2Id(), result.getPlayer2Id());
    }

    @Test
    void updateTeam_ShouldThrowEntityNotFoundException_WhenTeamDoesNotExist() {
        // Arrange
        Player player1 = new Player("John Doe");
        player1.setId(1L);

        Player player2 = new Player("Jane Doe");
        player2.setId(3L);

        List<Team> teams = new ArrayList<>();

        Team nonExistingTeam = new Team();
        nonExistingTeam.setId(100L);

        Team updatedTeam = new Team(player1.getId(), player2.getId());
        updatedTeam.setId(100L);

        when(playerRepository.getPlayer(updatedTeam.getPlayer1Id())).thenReturn(Optional.of(player1));
        when(playerRepository.getPlayer(updatedTeam.getPlayer2Id())).thenReturn(Optional.of(player2));
        when(teamRepository.getAllTeams()).thenReturn(teams);
        when(teamRepository.getTeam(nonExistingTeam.getId())).thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> teamService.updateTeam(nonExistingTeam.getId(), updatedTeam),
                "Expected EntityNotFoundException but none was thrown."
        );
        assertEquals("TeamService: Team with id " + nonExistingTeam.getId() + " not found.",
                thrown.getMessage());
    }

    @Test
    void deleteTeam_ShouldReturnTrue_WhenTeamExists() {
        // Arrange
        Long teamId = 1L;

        when(teamRepository.deleteTeam(teamId)).thenReturn(true);

        // Act
        boolean isDeleted = teamService.deleteTeam(teamId);

        // Assert
        assertTrue(isDeleted);
    }

    @Test
    void deleteTeam_ShouldThrowEntityNotFoundException_WhenTeamDoesNotExist() {
        // Arrange
        Long teamId = 99L;

        when(teamRepository.deleteTeam(teamId)).thenReturn(false);

        // Act + Assert
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> teamService.deleteTeam(teamId),
                "Expected EntityNotFoundException but none was thrown."
        );
        assertEquals("TeamService: Team with id " + teamId + " not found.",
                thrown.getMessage());
    }
}
