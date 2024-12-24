package com.football.Table_Football_Scorekeeper_API.Services;

import com.football.Table_Football_Scorekeeper_API.Entities.Game;

import java.util.List;
import java.util.Optional;

public interface GameService {

    Game addGame(Game game);
    Optional<Game> getGame(Long id);
    List<Game> getAllGames();
    Optional<Game> updateGame(Long id, Game game);
    boolean deleteGame(Long id);

}
