package com.football.Table_Football_Scorekeeper_API.Services;

import com.football.Table_Football_Scorekeeper_API.Entities.Player;
import com.football.Table_Football_Scorekeeper_API.Exceptions.EntityNotFoundException;
import com.football.Table_Football_Scorekeeper_API.Exceptions.ValidationException;
import com.football.Table_Football_Scorekeeper_API.Repositories.InMemoryPlayerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PlayerServiceImplTest {

    private PlayerServiceImpl playerService;
    private InMemoryPlayerRepository playerRepository;

    @BeforeEach
    void setUp() {
        // Initialize the in-memory repository and service
        playerRepository = new InMemoryPlayerRepository();
        playerService = new PlayerServiceImpl(playerRepository);

        // Add initial players for testing
        Player player1 = new Player("John Doe");
        player1.setId(1L);
        Player player2 = new Player("Jane Smith");
        player2.setId(2L);

        playerRepository.addPlayer(player1);
        playerRepository.addPlayer(player2);
    }

    @AfterEach
    void tearDown() {
        playerRepository.clearPlayers();  // Ensures the repository is reset after each test
    }

    @Test
    void addPlayer_ShouldAddNewPlayer() {
        // Arrange
        Player newPlayer = new Player("Test Name");

        // Act
        Player addedPlayer = playerService.addPlayer(newPlayer);

        // Assert
        assertEquals(3, addedPlayer.getId());
        assertEquals("Test Name", addedPlayer.getName());
        assertEquals(3, playerRepository.getAllPlayers().size());
    }


    @Test
    void addPlayer_ShouldReturnValidationException_WhenNameIsNull() {
        // Arrange
        Player newPlayer = new Player(null);

        // Act + Assert
        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> playerService.addPlayer(newPlayer),
                "Expected ValidationException but none was thrown"
        );
        assertEquals("PlayerService: Player name cannot be null or empty.", thrown.getMessage());  // Messages should match
    }


    @Test
    void addPlayer_ShouldReturnValidationException_WhenNameIsEmpty() {
        // Arrange
        Player newPlayer = new Player("");  // name is empty

        // Act + Assert
        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> playerService.addPlayer(newPlayer),
                "Expected ValidationException but none was thrown"
        );
        assertEquals("PlayerService: Player name cannot be null or empty.", thrown.getMessage());  // Messages should match
    }

    @Test
    void getPlayer_ShouldReturnPlayerById_WhenPlayerExists() {

        // Arrange
        Long playerId = 1L;  // This belongs to the player named John Doe

        // Act
        Player retrievedPlayer = playerService.getPlayer(playerId);

        // Assert
        assertNotNull(retrievedPlayer, "Player should not be null.");
        assertEquals("John Doe", retrievedPlayer.getName(), "Player name should match");
        assertEquals(playerId, retrievedPlayer.getId(), "Player ID should match");
    }

    @Test
    void getPlayer_ShouldThrowEntityNotFoundException_WhenPlayerDoesNotExist() {
        // Arrange
        Long playerId = 99L;


        // Act + Assert
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> playerService.getPlayer(playerId),
                "Expected EntityNotFoundException but none was thrown"
        );
        assertEquals("PlayerService: Player with id " + playerId + " not found.", thrown.getMessage());  // Messages should match
    }

    @Test
    void getAllPlayers_ShouldReturnAllPlayers() {
        // Act
        List<Player> allPlayers = playerService.getAllPlayers();

        // Assert
        assertEquals(2, allPlayers.size());
    }

    @Test
    void updatePlayer_ShouldUpdatePlayer_WhenPlayerExists() {
        // Arrange
        Player updatedPlayer = new Player("Updated John Doe");
        Long playerId = 1L;  // a player "John Doe" with this ID exists

        // Act
        Player playerToBeUpdated = playerService.updatePlayer(playerId, updatedPlayer);

        // Assert
        assertNotNull(playerToBeUpdated);
        assertEquals("Updated John Doe", playerToBeUpdated.getName());
    }

    @Test
    void deletePlayer_ShouldRemovePlayer_WhenPlayerExists() {  // Ensures a player is removed from the repository when they exist.
        // Arrange
        Long playerId = 1L;  // a player John Doe with this ID exists

        // Act
        boolean isDeleted = playerService.deletePlayer(playerId);

        // Assert
        assertTrue(isDeleted, "Expected John to be deleted.");
        assertEquals(1, playerRepository.getAllPlayers().size(), "Only one player should remain.");
        assertFalse(playerRepository.getAllPlayers().stream().anyMatch(p -> p.getId().equals(playerId)), "John " +
                "should no longer exist in repository.");
        assertTrue(playerRepository.getAllPlayers().stream().anyMatch(p -> p.getId().equals(2L)), "Jane Smith should " +
                "still exist in repository.");
    }

    @Test
    void getPlayersByName_ShouldReturnPlayers_WhenNameMatches() {
        // Arrange
        String name = "John Doe";

        // Act
        List<Player> playersNamedJohnDoe = playerService.findByNameIgnoreCase(name);

        // Assert
        assertEquals(1, playersNamedJohnDoe.size());
        assertEquals("John Doe", playersNamedJohnDoe.get(0).getName());
    }

    @Test
    void getPlayersByName_ShouldReturnEmptyList_WhenNoMatchFound() {
        // Arrange
        String name = "Nonexistent Name";

        // Act
        List<Player> noPlayersWithThatName = playerService.findByNameIgnoreCase(name);

        // Assert
        assertTrue(noPlayersWithThatName.isEmpty());

    }
}