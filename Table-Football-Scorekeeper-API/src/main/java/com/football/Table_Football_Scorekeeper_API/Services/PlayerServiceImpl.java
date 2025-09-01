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
        // Validate the player object, will throw ValidationException if invalid
        validatePlayer(player);

        // If validation passes, delegate the operation to the repository
        return playerRepository.addPlayer(player);
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
        return playerRepository.getAllPlayers();
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
        if (playerRepository.deletePlayer(id)) {
            return true;
        }
        throw new EntityNotFoundException("PlayerService: Player with id " + id + " not found.");
    }

    @Override
    public List<Player> findByNameIgnoreCase(String name) {
        return playerRepository.findByNameIgnoreCase(name);
    }

    private boolean validatePlayer(Player player) {
        // Validate that the player's name is not null or empty
        if (player.getName() == null || player.getName().isEmpty()) {
            throw new ValidationException("PlayerService: Player name cannot be null or empty.");
        }
        // Validate that the player's name is unique
        List<Player> players = getAllPlayers();
        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(player.getName())) {
                throw new ValidationException("PlayerService: A player with this exact name already exists. Pick a " +
                        "unique name!");
            }
        }
        return true;
    }
}
