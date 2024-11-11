package com.football.Table_Football_Scorekeeper_API.Repositories;

import com.football.Table_Football_Scorekeeper_API.Entities.Player;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryPlayerRepository implements PlayerRepository {

    private final List<Player> players = new ArrayList<Player>();
    private Long currentId = 0L;  // to simulate auto-increment ID

    // Helper method for sorting
    private List<Player> applySorting(Sort sort) {
        List<Player> sortedPlayers = new ArrayList<>(players);
        sort.forEach(order -> {
            if (order.getProperty().equals("name")) {
                sortedPlayers.sort(order.isAscending() ?
                        Comparator.comparing(Player::getName) :
                        Comparator.comparing(Player::getName).reversed());
            } else if (order.getProperty().equals("playerId")) {
                sortedPlayers.sort(order.isAscending() ?
                        Comparator.comparing(Player::getPlayerId) :
                        Comparator.comparing(Player::getPlayerId).reversed());
            }
        });
        return sortedPlayers;
    }

    @Override
    public Player addPlayer(Player player) {
        if (player.getPlayerId() == null) {
            player.setPlayerId(currentId++);
        }
        players.add(player);
        return player;
    }

    @Override
    public Optional<Player> getPlayer(Long id) {
        return players.stream()
                .filter(player -> player.getPlayerId().equals(id))
                .findFirst();
    }

    @Override
    public List<Player> getAllPlayers(Sort sort) {  // sort by a player object's field
        if (sort != null) {
            return applySorting(sort);
        }
        return new ArrayList<>(players);
    }

    @Override
    public Optional<Player> updatePlayer(Long id, String name) {
        for (Player player : players) {
            if (player.getPlayerId().equals(id)) {
                player.setName(name);
                return Optional.of(player);
            } else {
                return null; // TODO: Exception about player not found.
            }
        }
        return null; // TODO: What to write here?
    }

    @Override
    public boolean deletePlayer(Long id) {
        return players.removeIf(player -> player.getPlayerId().equals(id));
    }

    @Override
    public List<Player> getPlayersByName(String name) {
        return players.stream()
                .filter(player -> player.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }
}
