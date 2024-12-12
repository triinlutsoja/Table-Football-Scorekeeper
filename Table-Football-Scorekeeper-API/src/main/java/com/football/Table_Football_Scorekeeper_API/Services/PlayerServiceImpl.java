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

    // DI through constructor
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Optional<Player> addPlayer(Player player) {
        // Validate that the player's name is not null
        if (player.getName() == null || player.getName().isEmpty()) {
            return Optional.empty();  // Return empty if name is null
        }
        return playerRepository.addPlayer(player);
    }
/* COMMENTING OUT
        // Check for duplicates
        List<Player> existingPlayers = playerRepository.findByNameIgnoreCase(player.getName());
        if (!existingPlayers.isEmpty() ) {
            // Return Optional.empty() if a duplicate exists
            return Optional.empty();
        }

        // Add the player if all conditions are met
        return playerRepository.addPlayer(player);
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
        if (player.getName() == null) {
            return Optional.empty();  // Avoid null names
        }

        // Check for duplicate names (ignore the player being updated)
        List<Player> existingPlayers = playerRepository.findByNameIgnoreCase(player.getName());
        if (!existingPlayers.isEmpty() && !existingPlayers.get(0).getPlayerId().equals(id)) {
            return Optional.empty();  // return empty if name already exists
        }
        Optional<Player> existingPlayer = playerRepository.getPlayer(id);

        // Proceed with the update if no conflicts
        return playerRepository.updatePlayer(id, player);
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
        List<Player> players = playerRepository.findByNameIgnoreCase(name);
        if (players.isEmpty()) {
            return List.of();
        }
        return players;
    }*/
}
