package com.football.Table_Football_Scorekeeper_API.Services;

import com.football.Table_Football_Scorekeeper_API.Entities.Player;
import com.football.Table_Football_Scorekeeper_API.Repositories.InMemoryPlayerRepository;
import com.football.Table_Football_Scorekeeper_API.Repositories.PlayerRepositoryImpl;
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
        playerRepository.addPlayer(new Player("John Doe"));
        playerRepository.addPlayer(new Player("Jane Smith"));
    }

    @Test
    void addPlayer_ShouldAddNewPlayer() {  // Verifies that a new player is correctly added to the repository and service.
        // Arrange
        Player newPlayer = new Player("Test Name");

        // Act
        Optional<Player> addedPlayer = playerService.addPlayer(newPlayer);

        // Assert
        assertEquals(newPlayer.getPlayerId(), addedPlayer.get().getPlayerId());
        assertEquals(newPlayer.getName(), addedPlayer.get().getName());
        assertEquals(3, playerRepository.getAllPlayers().size());
    }

    @Test
    void getPlayer_ShouldReturnPlayer_WhenPlayerExists() {  // Ensures the correct player is returned if they exist in the repository.
        // Arrange
        Long playerId = 1L;

        // Act
        Optional<Player> foundPlayer = playerService.getPlayer(playerId);

        // Assert
        assertTrue(foundPlayer.isPresent());
        assertEquals("John Doe", foundPlayer.get().getName());
    }

    @Test
    void getPlayer_ShouldReturnEmptyOptional_WhenPlayerDoesNotExist() {  // Tests that an empty Optional is returned when the requested player ID does not exist.
        // Arrange
        Long playerId = 99L;

        // Act
        Optional<Player> nonExistingPlayer = playerService.getPlayer(playerId);

        // Assert
        assertTrue(nonExistingPlayer.isEmpty());
    }

    @Test
    void getAllPlayers_ShouldReturnAllPlayers() {  // Confirms that all players in the repository are returned.
        // Act
        List<Player> allPlayers = playerService.getAllPlayers();

        // Assert
        assertEquals(2, allPlayers.size());
    }

    @Test
    void updatePlayer_ShouldUpdatePlayer_WhenPlayerExists() { // Verifies that an existing player’s details are updated correctly.
        // Arrange
        Player updatedPlayer = new Player("Updated John Doe");
        Long playerId = 1L;  // a player with this ID exists

        // Act
        Optional<Player> playerToBeUpdated = playerService.updatePlayer(playerId, updatedPlayer);

        // Assert
        assertTrue(playerToBeUpdated.isPresent());
        assertEquals("Updated John Doe", playerToBeUpdated.get().getName());
    }

    @Test
    void updatePlayer_ShouldReturnEmptyOptional_WhenPlayerDoesNotExist() {  // Tests that an empty Optional is returned when trying to update a nonexistent player.
        // Arrange
        Player updatedPlayer = new Player("Updated John Doe");
        Long playerId = 99L;  // a player with this ID does not exist

        // Act
        Optional<Player> nonExistingPlayerToBeUpdated = playerService.updatePlayer(playerId, updatedPlayer);

        // Assert
        assertTrue(nonExistingPlayerToBeUpdated.isEmpty());
    }

    @Test
    void deletePlayer_ShouldRemovePlayer_WhenPlayerExists() {  // Ensures a player is removed from the repository when they exist.
        // Arrange
        Long playerId = 1L;  // a player with this ID exists

        // Act
        boolean playerToBeDeleted = playerService.deletePlayer(playerId);

        // Assert
        assertTrue(playerToBeDeleted);
        assertEquals(1, playerRepository.getAllPlayers().size());
    }

    @Test
    void deletePlayer_ShouldNotThrowError_WhenPlayerDoesNotExist() {  // Confirms that deleting a nonexistent player does not throw an error.
        // Arrange
        Long playerId = 99L;  // a player with this ID does not exist

        // Act
        boolean playerToBeDeleted = playerService.deletePlayer(playerId);

        // Assert
        assertFalse(playerToBeDeleted);
        assertEquals(2, playerRepository.getAllPlayers().size());
    }

    @Test
    void getPlayersByName_ShouldReturnPlayers_WhenNameMatches() {  // Checks that the correct players are returned based on the name search.
        // Arrange
        String name = "John Doe";

        // Act
        List<Player> playersNamedJohnDoe = playerService.getPlayersByName(name);

        // Assert
        assertEquals(1, playersNamedJohnDoe.size());
        assertEquals("John Doe", playersNamedJohnDoe.get(0).getName());
    }

    @Test
    void getPlayersByName_ShouldReturnEmptyList_WhenNoMatchFound() {  // Verifies that an empty list is returned when no players match the given name.
        // Arrange
        String name = "Nonexistent Name";

        // Act
        List<Player> noPlayersWithThatName = playerService.getPlayersByName(name);

        // Assert
        assertTrue(noPlayersWithThatName.isEmpty());

    }
}