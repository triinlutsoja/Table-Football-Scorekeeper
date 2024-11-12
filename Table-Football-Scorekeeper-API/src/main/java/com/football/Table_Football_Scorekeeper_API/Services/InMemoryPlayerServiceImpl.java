package com.football.Table_Football_Scorekeeper_API.Services;

import com.football.Table_Football_Scorekeeper_API.Entities.Player;
import com.football.Table_Football_Scorekeeper_API.Exceptions.DuplicatePlayerException;
import com.football.Table_Football_Scorekeeper_API.Exceptions.PlayerNotFoundException;
import com.football.Table_Football_Scorekeeper_API.Repositories.PlayerRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InMemoryPlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    // Constructor to inject the repository
    public InMemoryPlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Player addPlayer(Player player) {
        List<Player> existingPlayers = playerRepository.getPlayersByName(player.getName());

        if (existingPlayers.isEmpty()) {
            return playerRepository.addPlayer(player);
        } else {
            throw new DuplicatePlayerException("Player with name " + player.getName() + " already exists.");
        }
    }

    @Override
    public Optional<Player> getPlayer(Long id) {
        return Optional.ofNullable(playerRepository.getPlayer(id)
                .orElseThrow(() -> new PlayerNotFoundException("Player with id " + id + " not found.")));
    }

    @Override
    public List<Player> getAllPlayers(Sort sort) {
        return playerRepository.getAllPlayers(sort);
    }

    @Override
    public Optional<Player> updatePlayer(Long id, String name) {
        Optional<Player> existingPlayer = playerRepository.getPlayer(id);

        if (existingPlayer.isPresent()) {
            return playerRepository.updatePlayer(id, name);
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
            throw new PlayerNotFoundException("Player with id " + id + " not found.");
        }
    }

    @Override
    public List<Player> getPlayersByName(String name) {
        List<Player> players = playerRepository.getPlayersByName(name);
        if (players.isEmpty()) {
            throw new PlayerNotFoundException("No players found with the name: " + name);
        }
        return players;
    }
}
