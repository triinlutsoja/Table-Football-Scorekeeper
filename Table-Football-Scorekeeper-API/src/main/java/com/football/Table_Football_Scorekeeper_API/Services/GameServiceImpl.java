package com.football.Table_Football_Scorekeeper_API.Services;

import com.football.Table_Football_Scorekeeper_API.Entities.Game;
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

    @Override
    public Game addGame(Game game) {
        return null;
    }

    /* COMMENTING OUT FOR NOW
    @Override
    public Optional<Game> getGame(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Game> getAllGames() {
        return List.of();
    }

    @Override
    public Optional<Game> updateGame(Long id, Game game) {
        return Optional.empty();
    }

    @Override
    public boolean deleteGame(Long id) {
        return false;
    }*/
}
