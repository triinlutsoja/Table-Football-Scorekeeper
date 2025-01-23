package com.football.Table_Football_Scorekeeper_API.Services;

import com.football.Table_Football_Scorekeeper_API.Entities.Player;
import com.football.Table_Football_Scorekeeper_API.Exceptions.ValidationException;
import com.football.Table_Football_Scorekeeper_API.Repositories.InMemoryPlayerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
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
        playerRepository.addPlayer(new Player("John Doe"));
        playerRepository.addPlayer(new Player("Jane Smith"));
    }

    @AfterEach
    void tearDown() {
        playerRepository.clearPlayers();  // Ensures the repository is reset after each test
    }

    @Test
    void addPlayer_ShouldAddNewPlayer() {  // TODO: camelCase? snake_case etc? Don't MIX! Stick to one.
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

        // Act
        // TODO: Remove try-catch, because I can use assertThrows or similar
        ValidationException thrown = null;
        try {
            playerService.addPlayer(newPlayer);
        } catch (ValidationException e) {
            thrown = e;
        }

        // Assert
        assertNotNull(thrown, "Expected ValidationException but none was thrown");  // if no exception gets thrown,
        // display message
        assertEquals("Player name cannot be null or empty.", thrown.getMessage());  // Messages should match
    }


    @Test
    void addPlayer_ShouldReturnValidationException_WhenNameIsEmpty() {
        // Arrange
        Player newPlayer = new Player("");  // name is empty

        // Act
        ValidationException thrown = null;
        try {
            playerService.addPlayer(newPlayer);
        } catch (ValidationException e) {
            thrown = e;
        }

        // Assert
        assertNotNull(thrown, "Expected ValidationException but none was thrown");  // if no exception gets thrown,
        // display message
        assertEquals("Player name cannot be null or empty.", thrown.getMessage());  // Messages should match
    }

    @Test
    void getPlayer_ShouldReturnPlayerById_WhenPlayerExists() {

        // Arrange
        Long playerId = 1L;  // This belongs to the player named John Doe

        // Act
        Optional<Player> retrievedPlayer = playerService.getPlayer(playerId);

        // Assert
        assertTrue(retrievedPlayer.isPresent());
        assertEquals("John Doe", retrievedPlayer.get().getName());
    }

    @Test
    void getPlayer_ShouldReturnEmptyOptional_WhenPlayerDoesNotExist() {
        // Arrange
        Long playerId = 99L;

        // Act
        Optional<Player> nonExistingPlayer = playerService.getPlayer(playerId);

        // Assert
        assertTrue(nonExistingPlayer.isEmpty());
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
        // TODO: It's better to first add a player, then delete it and make sure that this specific player doesn't
        //  exist anymore.
        Long playerId = 1L;  // a player with this ID exists

        // Act
        boolean isDeleted = playerService.deletePlayer(playerId);

        // Assert
        assertTrue(isDeleted);
        assertEquals(1, playerRepository.getAllPlayers().size());
        // TODO: Make sure that John is deleted, and not Peter. Now you can't be sure, who got deleted.
    }


    @Test
    void deletePlayer_ShouldReturnFalse_WhenPlayerDoesNotExist() {
        // TODO: This test becomes redundant because this scenario is already tested with getPlayer.
        
        // Arrange
        Long playerId = 99L;  // a player with this ID does not exist

        // Act
        boolean isDeleted = playerService.deletePlayer(playerId);

        // Assert
        assertFalse(isDeleted);
        assertEquals(2, playerRepository.getAllPlayers().size());
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