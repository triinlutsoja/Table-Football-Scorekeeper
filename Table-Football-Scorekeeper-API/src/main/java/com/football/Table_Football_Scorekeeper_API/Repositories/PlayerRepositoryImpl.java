package com.football.Table_Football_Scorekeeper_API.Repositories;

import com.football.Table_Football_Scorekeeper_API.Entities.Player;

import java.util.List;
import java.util.Optional;

public class PlayerRepositoryImpl implements PlayerRepository {


    @Override
    public Optional<Player> addPlayer(Player player) {
        return null;  // TODO:
    }

    @Override
    public Optional<Player> getPlayer(Long id) {
        return Optional.empty();  // TODO:
    }

    @Override
    public List<Player> getAllPlayers() {
        return List.of();  // TODO:
    }

    @Override
    public Optional<Player> updatePlayer(Long id, Player player) {
        return Optional.empty();  // TODO:
    }

    @Override
    public boolean deletePlayer(Long id) {
        return false;  // TODO:
    }

    @Override
    public List<Player> getPlayersByName(String name) {
        return List.of();  // TODO:
    }
}
