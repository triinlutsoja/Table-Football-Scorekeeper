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

    @Override
    public Optional<Player> updatePlayer(Long id, Player player) {

        // Validate that the player's name is not null or empty
        if (player.getName() == null || player.getName().isEmpty()) {
            throw new ValidationException("Player name cannot be null or empty.");
        }

        try {
            return playerRepository.updatePlayer(id, player);
        } catch (RuntimeException e) {
            System.err.println("Failed to update player: " + e.getMessage());
            throw new RuntimeException("PlayerService failed to update player.", e);
        }
    }

    @Override
    public boolean deletePlayer(Long id) {
        try {
            return playerRepository.deletePlayer(id);
        } catch (RuntimeException e) {
            System.err.println("Failed to delete player: " + e.getMessage());
            throw new RuntimeException("PlayerService failed to delete player.", e);
        }
    }

    @Override
    public List<Player> findByNameIgnoreCase(String name) {
        try {
            return playerRepository.findByNameIgnoreCase(name);
        } catch (RuntimeException e) {
            System.err.println("Failed to retrieve all players with the name " + name + "." + e.getMessage());
            throw new RuntimeException("PlayerService failed to retrieve all players with a specific name.", e);
        }
    }
}
