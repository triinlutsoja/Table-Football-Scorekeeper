package com.football.Table_Football_Scorekeeper_API.Services;
import com.football.Table_Football_Scorekeeper_API.Entities.Player;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface PlayerService {

    Player addPlayer(Player player);

    Optional<Player> getPlayer(Long id);

    List<Player> getAllPlayers(Sort sort);

    Optional<Player> updatePlayer(Long id, String name);

    boolean deletePlayer(Long id);

    // Custom methods

    List<Player> getPlayersByName(String name);
}
