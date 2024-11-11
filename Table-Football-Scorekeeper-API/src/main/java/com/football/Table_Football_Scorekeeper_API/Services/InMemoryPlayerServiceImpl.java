package com.football.Table_Football_Scorekeeper_API.Services;

import com.football.Table_Football_Scorekeeper_API.Entities.Player;
import com.football.Table_Football_Scorekeeper_API.Repositories.InMemoryPlayerRepository;
import com.football.Table_Football_Scorekeeper_API.Repositories.PlayerRepository;

import java.util.List;
import java.util.Optional;

public class InMemoryPlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository = new InMemoryPlayerRepository();

    @Override
    public Player addPlayer(Player player) {
        // TODO: Double check that this player doesn't already exist to avoid duplicates.
        return playerRepository.addPlayer(player);
    }

    @Override
    public Optional<Player> getPlayer(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Player> getAllPlayers() {
        return List.of();
    }

    @Override
    public Optional<Player> updatePlayer(Long id, String name) {
        return Optional.empty();
    }

    @Override
    public boolean deletePlayer(Long id) {
        return false;
    }

    @Override
    public List<Player> getPlayersByName(String name) {
        return List.of();
    }
}
