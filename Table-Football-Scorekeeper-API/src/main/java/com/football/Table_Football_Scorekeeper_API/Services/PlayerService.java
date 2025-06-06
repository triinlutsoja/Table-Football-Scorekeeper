package com.football.Table_Football_Scorekeeper_API.Services;
import com.football.Table_Football_Scorekeeper_API.Entities.Player;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PlayerService {

    Player addPlayer(Player player);
    Optional<Player> getPlayer(Long id);
    List<Player> getAllPlayers();
    Player updatePlayer(Long id, Player player);
    boolean deletePlayer(Long id);

    // Custom methods
    List<Player> findByNameIgnoreCase(String name);
}
