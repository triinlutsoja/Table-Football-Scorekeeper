package com.football.Table_Football_Scorekeeper_API.Services;

import com.football.Table_Football_Scorekeeper_API.Entities.Player;
import com.football.Table_Football_Scorekeeper_API.Exceptions.ValidationException;
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
    public Player addPlayer(Player player) {

        // Validate that the player's name is not null or empty
        if (player.getName() == null || player.getName().isEmpty()) {
            throw new ValidationException("Player name cannot be null or empty.");
        }

        try {
            return playerRepository.addPlayer(player);
        } catch (RuntimeException e) {
            System.err.println("Failed to add player: " + e.getMessage());
            throw new RuntimeException("PlayerService failed to add player.", e);
        }
    }

    @Override
    public Optional<Player> getPlayer(Long id) {
        try {
            return playerRepository.getPlayer(id);
        } catch (RuntimeException e) {
            System.err.println("Failed to retrieve player: " + e.getMessage());
            throw new RuntimeException("PlayerService failed to retrieve player.", e);
        }
    }

    @Override
    public List<Player> getAllPlayers() {
        try {
            return playerRepository.getAllPlayers();
        } catch (RuntimeException e) {
            System.err.println("Failed to retrieve all players: " + e.getMessage());
            throw new RuntimeException("PlayerService failed to retrieve all players.", e);
        }
    }
    /* COMMENTING OUT

    @Override
    public Optional<Player> updatePlayer(Long id, Player player) {
        if (player.getName() == null) {
            return Optional.empty();  // Avoid null names
        }

        // Check for duplicate names (ignore the player being updated)
        List<Player> existingPlayers = playerRepository.findByNameIgnoreCase(player.getName());
        if (!existingPlayers.isEmpty() && !existingPlayers.get(0).getId().equals(id)) {
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
