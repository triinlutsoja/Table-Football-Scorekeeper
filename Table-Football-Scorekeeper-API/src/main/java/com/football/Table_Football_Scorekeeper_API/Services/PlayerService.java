package com.football.Table_Football_Scorekeeper_API.Services;
import com.football.Table_Football_Scorekeeper_API.Entities.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerService {

    Player addPlayer(Player player);

    Optional<Player> getPlayer(int id);

    List<Player> getAllPlayers();

    Optional<Player> updatePlayer(int id, String name);

    boolean deletePlayer(int id);

    // Custom methods

    List<Player> getPlayersByName(String name);
}
