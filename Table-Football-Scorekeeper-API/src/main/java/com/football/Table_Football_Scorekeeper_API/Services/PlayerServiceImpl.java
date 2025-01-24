package com.football.Table_Football_Scorekeeper_API.Services;

import com.football.Table_Football_Scorekeeper_API.Entities.Player;
import com.football.Table_Football_Scorekeeper_API.Exceptions.EntityNotFoundException;
import com.football.Table_Football_Scorekeeper_API.Exceptions.ValidationException;
import com.football.Table_Football_Scorekeeper_API.Repositories.PlayerRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
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
        if (validatePlayer(player)) {
            return playerRepository.addPlayer(player);
        }
        throw new RuntimeException("PlayerService: Failed to add player.");
    }

    @Override
    public Optional<Player> getPlayer(Long id) {
        Optional<Player> retrievedPlayer = playerRepository.getPlayer(id);
        if (retrievedPlayer.isPresent()) {
            return retrievedPlayer;
        }
        throw new EntityNotFoundException("PlayerService: Player with id " + id + " not found.");
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
    public Player updatePlayer(Long id, Player player) {
        // Validate player
        validatePlayer(player);

        // Validate that player exists to proceed with update
        if (getPlayer(id).isPresent()) {
            return playerRepository.updatePlayer(id, player);
        }
        throw new EntityNotFoundException("PlayerService: Player with id " + id + " not found.");
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

    private boolean validatePlayer(Player player) {
        // Validate that the player's name is not null or empty
        if (player.getName() == null || player.getName().isEmpty()) {
            throw new ValidationException("PlayerService: Player name cannot be null or empty.");
        }
        return true;
    }
}
