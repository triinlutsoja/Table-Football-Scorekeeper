package com.football.Table_Football_Scorekeeper_API.Services;

import com.football.Table_Football_Scorekeeper_API.Entities.Game;
import com.football.Table_Football_Scorekeeper_API.Exceptions.EntityNotFoundException;
import com.football.Table_Football_Scorekeeper_API.Exceptions.ValidationException;
import com.football.Table_Football_Scorekeeper_API.Repositories.GameRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceImplTest {

    private GameRepository gameRepository;
    private GameServiceImpl gameService;

    @BeforeEach
    public void setUp() {
        // Create a mock of GameRepository
        gameRepository = Mockito.mock(GameRepository.class);

        // Pass the mock to the service
        gameService = new GameServiceImpl(gameRepository);
    }

    @Test
    void addGame_ShouldSaveGame() {
        // Arrange
        Game game = new Game(1L, 8, 4, 2L);
        Game savedGame = new Game(LocalDateTime.now(),2L, 1L, 8, 4);
        savedGame.setId(100L);  // mock repository's behaviour of automatically adding an ID

        when(gameRepository.addGame(game)).thenReturn(savedGame);  // Mock repository addGame behavior

        // Act
        Game result = gameService.addGame(game);

        // Assert
        Assertions.assertNotNull(result, "The saved game should not be null");
        Assertions.assertEquals(8, result.getScoreGrey());
        Assertions.assertEquals(4, result.getScoreBlack());
        Assertions.assertEquals(2L, result.getGreyId());
        Assertions.assertEquals(1L, result.getBlackId());
        verify(gameRepository, times(1)).addGame(game); // Verify addGame() is called
    }

    @Test
    void addGame_ShouldReturnValidationException_WhenGreyIdIsNull() {
        // Arrange
        Game invalidGame = new Game(1L, 8, 4, null);

        // Act
        ValidationException thrown = null;
        try {
            gameService.addGame(invalidGame); // Method call that should throw an exception
        } catch (ValidationException e) {
            thrown = e;
        }

        // Assert
        assertNotNull(thrown, "Expected ValidationException but none was thrown");  // if no exception gets thrown,
        // display message
        assertEquals("ID of the grey player cannot be null or less than or equal to zero.", thrown.getMessage());  //
        // Messages should match
    }

    @Test
    void addGame_ShouldReturnValidationException_WhenGreyIdIsLessThanZero() {
        // Arrange
        Game invalidGame = new Game(1L, 8, 4, -1L);

        // Act
        ValidationException thrown = null;
        try {
            gameService.addGame(invalidGame); // Method call that should throw an exception
        } catch (ValidationException e) {
            thrown = e;
        }

        // Assert
        assertNotNull(thrown, "Expected ValidationException but none was thrown");  // if no exception gets thrown,
        // display message
        assertEquals("ID of the grey player cannot be null or less than or equal to zero.", thrown.getMessage());  //
        // Messages should match
    }

    @Test
    void addGame_ShouldReturnValidationException_WhenGreyIdIsEqualToZero() {
        // Arrange
        Game invalidGame = new Game(1L, 8, 4, 0L);

        // Act
        ValidationException thrown = null;
        try {
            gameService.addGame(invalidGame); // Method call that should throw an exception
        } catch (ValidationException e) {
            thrown = e;
        }

        // Assert
        assertNotNull(thrown, "Expected ValidationException but none was thrown");  // if no exception gets thrown,
        // display message
        assertEquals("ID of the grey player cannot be null or less than or equal to zero.", thrown.getMessage());  //
        // Messages should match
    }

    @Test
    void addGame_ShouldReturnValidationException_WhenBlackIdIsNull() {
        // Arrange
        Game invalidGame = new Game(null, 8, 4, 1L);

        // Act
        ValidationException thrown = null;
        try {
            gameService.addGame(invalidGame); // Method call that should throw an exception
        } catch (ValidationException e) {
            thrown = e;
        }

        // Assert
        assertNotNull(thrown, "Expected ValidationException but none was thrown");  // if no exception gets thrown,
        // display message
        assertEquals("ID of the black player cannot be null or less than or equal to zero.", thrown.getMessage());  //
        // Messages should match
    }

    @Test
    void addGame_ShouldReturnValidationException_WhenBlackIdIsLessThanZero() {
        // Arrange
        Game invalidGame = new Game(-1L, 8, 4, 1L);

        // Act
        ValidationException thrown = null;
        try {
            gameService.addGame(invalidGame); // Method call that should throw an exception
        } catch (ValidationException e) {
            thrown = e;
        }

        // Assert
        assertNotNull(thrown, "Expected ValidationException but none was thrown");  // if no exception gets thrown,
        // display message
        assertEquals("ID of the black player cannot be null or less than or equal to zero.", thrown.getMessage());  //
        // Messages should match
    }

    @Test
    void addGame_ShouldReturnValidationException_WhenBlackIdIsEqualToZero() {
        // Arrange
        Game invalidGame = new Game(0L, 8, 4, 1L);

        // Act
        ValidationException thrown = null;
        try {
            gameService.addGame(invalidGame); // Method call that should throw an exception
        } catch (ValidationException e) {
            thrown = e;
        }

        // Assert
        assertNotNull(thrown, "Expected ValidationException but none was thrown");  // if no exception gets thrown,
        // display message
        assertEquals("ID of the black player cannot be null or less than or equal to zero.", thrown.getMessage());  //
        // Messages should match
    }

    @Test
    void addGame_ShouldReturnValidationException_WhenPlayerTriesToPlayAgainstSelf() {
        // Arrange
        Game invalidGame = new Game(1L, 8, 4, 1L);

        // Act
        ValidationException thrown = null;
        try {
            gameService.addGame(invalidGame); // Method call that should throw an exception
        } catch (ValidationException e) {
            thrown = e;
        }

        // Assert
        assertNotNull(thrown, "Expected ValidationException but none was thrown");  // if no exception gets thrown,
        // display message
        assertEquals("Players can't play against themselves.", thrown.getMessage());  // Messages should match
    }

    @Test
    void addGame_ShouldReturnValidationException_WhenScoreGreyIsLessThanZero() {
        // Arrange
        Game invalidGame = new Game(1L, -1, 8, 2L);

        // Act
        ValidationException thrown = null;
        try {
            gameService.addGame(invalidGame); // Method call that should throw an exception
        } catch (ValidationException e) {
            thrown = e;
        }

        // Assert
        assertNotNull(thrown, "Expected ValidationException but none was thrown");  // if no exception gets thrown,
        // display message
        assertEquals("Score of the grey player cannot be less than zero or more than 8.", thrown.getMessage());  // Messages should match
    }

    @Test
    void addGame_ShouldReturnValidationException_WhenScoreGreyIsMoreThanEight() {
        // Arrange
        Game invalidGame = new Game(1L, 9, 8, 2L);

        // Act
        ValidationException thrown = null;
        try {
            gameService.addGame(invalidGame); // Method call that should throw an exception
        } catch (ValidationException e) {
            thrown = e;
        }

        // Assert
        assertNotNull(thrown, "Expected ValidationException but none was thrown");  // if no exception gets thrown,
        // display message
        assertEquals("Score of the grey player cannot be less than zero or more than 8.", thrown.getMessage());  // Messages should match
    }

    @Test
    void addGame_ShouldReturnValidationException_WhenScoreBlackIsLessThanZero() {
        // Arrange
        Game invalidGame = new Game(1L, 7, -1, 2L);

        // Act
        ValidationException thrown = null;
        try {
            gameService.addGame(invalidGame); // Method call that should throw an exception
        } catch (ValidationException e) {
            thrown = e;
        }

        // Assert
        assertNotNull(thrown, "Expected ValidationException but none was thrown");  // if no exception gets thrown,
        // display message
        assertEquals("Score of the black player cannot be less than zero or more than 8.", thrown.getMessage());  //
        // Messages should match
    }

    @Test
    void addGame_ShouldReturnValidationException_WhenScoreBlackIsMoreThanEight() {
        // Arrange
        Game invalidGame = new Game(1L, 7, 9, 2L);

        // Act
        ValidationException thrown = null;
        try {
            gameService.addGame(invalidGame); // Method call that should throw an exception
        } catch (ValidationException e) {
            thrown = e;
        }

        // Assert
        assertNotNull(thrown, "Expected ValidationException but none was thrown");  // if no exception gets thrown,
        // display message
        assertEquals("Score of the black player cannot be less than zero or more than 8.", thrown.getMessage());  //
        // Messages should match
    }

    @Test
    void addGame_ShouldReturnValidationException_WhenGameIsNotFinished() {
        // Arrange
        Game invalidGame = new Game(1L, 4, 5, 2L);

        // Act
        ValidationException thrown = null;
        try {
            gameService.addGame(invalidGame); // Method call that should throw an exception
        } catch (ValidationException e) {
            thrown = e;
        }

        // Assert
        assertNotNull(thrown, "Expected ValidationException but none was thrown");  // if no exception gets thrown,
        // display message
        assertEquals("The game is not finished. One player must reach exactly 8 points, and the other must have fewer than 8.", thrown.getMessage());  //
        // Messages should match
    }

    @Test
    void getGame_ShouldReturnGameById_WhenGameExists() {
        // Arrange
        Game game = new Game(1L, 8, 4, 2L);
        Game savedGame = new Game(LocalDateTime.now(),2L, 1L, 8, 4);
        savedGame.setId(100L);  // mock repository's behaviour of automatically adding an ID

        when(gameRepository.getGame(100L)).thenReturn(Optional.of(savedGame));  // Mock repository getGame behavior

        // Act
        Optional<Game> retrievedGame = gameService.getGame(100L);

        // Assert
        assertTrue(retrievedGame.isPresent());
        assertEquals(savedGame, retrievedGame.get());
    }

    @Test
    void getGame_ShouldThrowEntityNotFoundException_WhenGameDoesNotExist() {
        // Arrange
        Long nonExistingGameId = 99L;

        when(gameRepository.getGame(nonExistingGameId)).thenReturn(Optional.empty());  // Mock repository getGame behavior

        // Act
        EntityNotFoundException thrown = null;
        try {
            gameService.getGame(nonExistingGameId);
        } catch (EntityNotFoundException e) {
            thrown = e;
        }

        // Assert
        assertNotNull(thrown, "Expected EntityNotFoundException but none was thrown");  // if no exception gets thrown,
        // display message
        assertEquals("GameService: Game with id " + nonExistingGameId + " not found.", thrown.getMessage());  //
        // Messages should match
    }

    @Test
    void getAllGames_ShouldReturnAllGames() {
        // Arrange
        List<Game> games = new ArrayList<>();

        Game existingGame1 = new Game(LocalDateTime.now(),2L, 1L, 8, 4);
        existingGame1.setId(1L);  // mock repository's behaviour of automatically adding an ID
        games.add(existingGame1);

        Game existingGame2 = new Game(LocalDateTime.now(),2L, 1L, 8, 4);
        existingGame2.setId(2L);  // mock repository's behaviour of automatically adding an ID
        games.add(existingGame2);

        when(gameRepository.getAllGames()).thenReturn(games);  // Mock repository getGame behavior

        // Act
        List<Game> retrievedGames = gameService.getAllGames();

        // Assert
        assertEquals(2, retrievedGames.size());
    }

    @Test
    void updateGame_ShouldUpdateGame_WhenGameExists() {
        // Arrange
        Game existingGame = new Game(LocalDateTime.now(),2L, 1L, 8, 4);
        existingGame.setId(1L);  // mock repository's behaviour of automatically adding an ID

        Game updatedGame = new Game(LocalDateTime.now(),2L, 1L, 4, 8);
        updatedGame.setId(1L);

        when(gameRepository.getGame(1L)).thenReturn(Optional.of(existingGame));  // Mock repository behaviour
        when(gameRepository.updateGame(1L, updatedGame)).thenReturn(updatedGame);  // Mock repository behaviour

        // Act
        Game retrievedGame = gameService.updateGame(1L, updatedGame);

        // Assert
        assertNotNull(retrievedGame);
        assertEquals(updatedGame.getId(), retrievedGame.getId());
        assertEquals(updatedGame.getScoreGrey(), retrievedGame.getScoreGrey());
        assertEquals(updatedGame.getScoreBlack(), retrievedGame.getScoreBlack());
    }

    @Test
    void deleteGame_ShouldDeleteGame_WhenGameExists() {
        // Arrange
        Long gameId = 1L;  // a game with this ID exists

        when(gameRepository.deleteGame(gameId)).thenReturn(true);  // Mock repository deleteGame behavior

        // Act
        boolean isDeleted = gameService.deleteGame(gameId);

        // Assert
        assertTrue(isDeleted);
    }

    @Test
    void deleteGame_ShouldReturnFalse_WhenGameDoesNotExist() {
        // Arrange
        Long gameId = 99L;  // a game with this ID does not exist

        when(gameRepository.deleteGame(gameId)).thenReturn(false);  // Mock repository deleteGame behavior

        // Act
        EntityNotFoundException thrown = null;
        try {
            gameService.deleteGame(gameId);
        } catch (EntityNotFoundException e) {
            thrown = e;
        }

        // Assert
        assertNotNull(thrown, "Expected EntityNotFoundException but none was thrown");
        assertEquals("GameService: Game with id " + gameId + " not found.", thrown.getMessage());  //
        // Messages should match
    }
}