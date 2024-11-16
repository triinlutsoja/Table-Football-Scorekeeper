package com.football.Table_Football_Scorekeeper_API.Services;

import com.football.Table_Football_Scorekeeper_API.Entities.Player;
import com.football.Table_Football_Scorekeeper_API.Repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    // Constructor to inject the repository
    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Optional<Player> addPlayer(Player player) {
        List<Player> existingPlayers = playerRepository.getPlayersByName(player.getName());
        
        if (existingPlayers.isEmpty()) {
            return playerRepository.addPlayer(player);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Player> getPlayer(Long id) {
        Optional<Player> existingPlayer = playerRepository.getPlayer(id);

        if (existingPlayer.isEmpty()) {
            return Optional.empty();
        }
        return existingPlayer;
    }

    @Override
    public List<Player> getAllPlayers() {
        List<Player> players = playerRepository.getAllPlayers();
        if (players.isEmpty()) {
            return List.of();
        }
        return players;
    }

    @Override
    public Optional<Player> updatePlayer(Long id, Player player) {
        Optional<Player> existingPlayer = playerRepository.getPlayer(id);

        if (existingPlayer.isPresent()) {
            return playerRepository.updatePlayer(id, player);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean deletePlayer(Long id) {
        Optional<Player> existingPlayer = playerRepository.getPlayer(id);

        if (existingPlayer.isPresent()) {
            return playerRepository.deletePlayer(id);
        } else {
            return false;
        }
    }

    @Override
    public List<Player> getPlayersByName(String name) {
        List<Player> players = playerRepository.getPlayersByName(name);
        if (players.isEmpty()) {
            return List.of();
        }
        return players;
    }
}
