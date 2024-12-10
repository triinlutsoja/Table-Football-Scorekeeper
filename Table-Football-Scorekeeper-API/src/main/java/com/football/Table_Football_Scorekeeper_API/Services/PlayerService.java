package com.football.Table_Football_Scorekeeper_API.Services;
import com.football.Table_Football_Scorekeeper_API.Entities.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerService {

    Optional<Player> addPlayer(Player player);
/* COMMENTING OUT
    Optional<Player> getPlayer(Long id);

    List<Player> getAllPlayers();

    Optional<Player> updatePlayer(Long id, Player player);

    boolean deletePlayer(Long id);

    // Custom methods

    List<Player> getPlayersByName(String name);*/
}
