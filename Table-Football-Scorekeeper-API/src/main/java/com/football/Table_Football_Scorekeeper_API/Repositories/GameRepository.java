package com.football.Table_Football_Scorekeeper_API.Repositories;

import com.football.Table_Football_Scorekeeper_API.Entities.Game;
import com.football.Table_Football_Scorekeeper_API.Entities.PlayerStat;

import java.util.List;
import java.util.Optional;

public interface GameRepository {

    Game addGame(Game game);
    Optional<Game> getGame(Long id);
    List<Game> getAllGames();
    Game updateGame(Long id, Game game);
    boolean deleteGame(Long id);
    List<PlayerStat> calculateWinStats();
}
