package com.football.Table_Football_Scorekeeper_API.Repositories;

import com.football.Table_Football_Scorekeeper_API.Entities.Player;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository {

    Optional<Player> addPlayer(Player player);

    Optional<Player> getPlayer(Long id);

    List<Player> getAllPlayers();

    Optional<Player> updatePlayer(Long id, Player player);

    boolean deletePlayer(Long id);

    // Custom query methods

    List<Player> getPlayersByName(String name);



}
