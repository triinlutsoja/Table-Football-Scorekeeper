package com.football.Table_Football_Scorekeeper_API.Services;

import com.football.Table_Football_Scorekeeper_API.Entities.Game;
import com.football.Table_Football_Scorekeeper_API.Exceptions.EntityNotFoundException;
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
            throw new ValidationException("ID of the grey team cannot be null or less than or equal to zero.");
        }
        if (game.getBlackId() == null || game.getBlackId() <= 0L) {
            throw new ValidationException("ID of the black team cannot be null or less than or equal to zero.");
        }
        if (game.getGreyId() == game.getBlackId()) {
            throw new ValidationException("Teams can't play against themselves.");
        }
        if (game.getScoreGrey() < 0 || game.getScoreGrey() > 8) {
            throw new ValidationException("Score of the grey team cannot be less than zero or more than 8.");
        }
        if (game.getScoreBlack() < 0 || game.getScoreBlack() > 8) {
            throw new ValidationException("Score of the black team cannot be less than zero or more than 8.");
        }
        boolean greyWins = (game.getScoreGrey() == 8 && game.getScoreBlack() < 8);
        boolean blackWins = (game.getScoreBlack() == 8 && game.getScoreGrey() < 8);
        if (!greyWins && !blackWins) {
            throw new ValidationException("The game is not finished. One team must reach exactly 8 points, and the " +
                    "other must have fewer than 8.");
        }
        return true;
    }

    @Override
    public Game addGame(Game game) {
        // Validate the game object, will throw ValidationException if invalid
        validateGame(game);

        // If validation passes, delegate the operation to the repository
        return gameRepository.addGame(game);
    }

    @Override
    public Optional<Game> getGame(Long id) {
        Optional<Game> retrievedGame = gameRepository.getGame(id);
        if (retrievedGame.isPresent()) {
            return retrievedGame;
        } else {
            throw new EntityNotFoundException("GameService: Game with id " + id + " not found.");
        }
    }

    @Override
    public List<Game> getAllGames() {
        return gameRepository.getAllGames();
    }

    @Override
    public Game updateGame(Long id, Game game) {
        // Validate game
        validateGame(game);

        // Validate that game exists to proceed with update
        if (getGame(id).isPresent()) {
            return gameRepository.updateGame(id, game);
        }
        throw new EntityNotFoundException("GameService: Game with id " + id + " not found.");
    }

    @Override
    public boolean deleteGame(Long id) {
       if (gameRepository.deleteGame(id)) {
           return true;
       } else {
           throw new EntityNotFoundException("GameService: Game with id " + id + " not found.");
       }
    }
}
