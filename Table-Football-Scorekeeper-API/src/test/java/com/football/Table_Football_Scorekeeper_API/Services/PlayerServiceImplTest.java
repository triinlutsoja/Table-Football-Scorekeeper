package com.football.Table_Football_Scorekeeper_API.Services;

import com.football.Table_Football_Scorekeeper_API.Entities.Player;
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
        playerRepository.addPlayer(new Player("John Doe"));
        playerRepository.addPlayer(new Player("Jane Smith"));
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
    void getPlayer_ShouldReturnPlayerById_WhenPlayerExists() {  // Ensures the correct player is returned if they exist
        // in the repository.

        // Arrange
        Long playerId = 1L;  // This belongs to the player named John Doe

        // Act
        Optional<Player> retrievedPlayer = playerService.getPlayer(playerId);

        // Assert
        assertTrue(retrievedPlayer.isPresent());
        assertEquals("John Doe", retrievedPlayer.get().getName());
    }
    /* COMMENTING OUT

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

    void updatePlayer_ShouldReturnEmptyOptional_WhenNameAlreadyExists() {  // Tests that an empty Optional is returned
        // when trying to update an existing player with a duplicate name.

        // Arrange
        Player updatedPlayer = new Player("Jame Smith");  // a "Jane Smith" already exists in setUp()
        Long playerId = 1L;  // a player with this ID exists

        // Act
        Optional<Player> result = playerService.updatePlayer(playerId, updatedPlayer);

        // Assert
        assertTrue(result.isEmpty(), "Updating a player to a name that already exists should return an empty Optional");
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

    }*/
}