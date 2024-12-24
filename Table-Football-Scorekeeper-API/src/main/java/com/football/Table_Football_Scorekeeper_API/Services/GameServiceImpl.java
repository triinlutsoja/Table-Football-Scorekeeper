package com.football.Table_Football_Scorekeeper_API.Services;

import com.football.Table_Football_Scorekeeper_API.Entities.Game;
import com.football.Table_Football_Scorekeeper_API.Exceptions.ValidationException;
import com.football.Table_Football_Scorekeeper_API.Repositories.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    private boolean validateGame(Game game) {
        if (game.getGreyId() == null || game.getGreyId() <= 0L) {
            throw new ValidationException("ID of the grey player cannot be null or less than or equal to zero.");
        }
        if (game.getBlackId() == null || game.getBlackId() <= 0L) {
            throw new ValidationException("ID of the black player cannot be null or less than or equal to zero.");
        }
        if (game.getGreyId() == game.getBlackId()) {
            throw new ValidationException("Players can't play against themselves.");
        }
        if (game.getScoreGrey() < 0 && game.getScoreGrey() > 8) {
            throw new ValidationException("Score of the grey player cannot be less than zero or more than 8.");
        }
        if (game.getScoreBlack() < 0 && game.getScoreBlack() > 8) {
            throw new ValidationException("Score of the black player cannot be less than zero or more than 8.");
        }
        boolean greyWins = (game.getScoreGrey() == 8 && game.getScoreBlack() < 8);
        boolean blackWins = (game.getScoreBlack() == 8 && game.getScoreGrey() < 8);
        if (!greyWins && !blackWins) {
            throw new ValidationException("The game is not finished. One player must reach exactly 8 points, and the other must have fewer than 8.");
        }
        return true;
    }

    @Override
    public Game addGame(Game game) {
        // Validate game
        validateGame(game);

        try {
            return gameRepository.addGame(game);
        } catch (RuntimeException e) {
            System.err.println("Failed to add game: " + e.getMessage());
            throw new RuntimeException("GameService failed to add game.", e);
        }
    }

    @Override
    public Optional<Game> getGame(Long id) {
        try {
            return gameRepository.getGame(id);
        } catch (RuntimeException e) {
            System.err.println("Failed to retrieve game: " + e.getMessage());
            throw new RuntimeException("GameService failed to retrieve game.", e);
        }
    }

    @Override
    public List<Game> getAllGames() {
        try {
            return gameRepository.getAllGames();
        } catch (RuntimeException e) {
            System.err.println("Failed to retrieve all games: " + e.getMessage());
            throw new RuntimeException("GameService failed to retrieve all games.", e);
        }
    }

    @Override
    public Optional<Game> updateGame(Long id, Game game) {

        // Validate game
        validateGame(game);

        try {
            return gameRepository.updateGame(id, game);
        } catch (RuntimeException e) {
            System.err.println("Failed to update game: " + e.getMessage());
            throw new RuntimeException("GameService failed to update game.", e);
        }
    }

    @Override
    public boolean deleteGame(Long id) {
        try {
            return gameRepository.deleteGame(id);
        } catch (RuntimeException e) {
            System.err.println("Failed to delete game: " + e.getMessage());
            throw new RuntimeException("GameService failed to delete game.", e);
        }
    }
}
