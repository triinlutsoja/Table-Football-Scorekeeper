package com.football.Table_Football_Scorekeeper_API.Repositories;

import com.football.Table_Football_Scorekeeper_API.Entities.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryPlayerRepository implements PlayerRepository {

    private final List<Player> players = new ArrayList<Player>();
    private Long currentId = 1L;  // to simulate auto-increment ID

    @Override
    public Player addPlayer(Player player) {
        if (player.getId() == null) {
            player.setId(currentId++);
        }
        players.add(player);
        return player;
    }

    @Override
    public Optional<Player> getPlayer(Long id) {
        return players.stream()
                .filter(player -> player.getId().equals(id))
                .findFirst();  // Returns Optional.empty() if not found
    }

    @Override
    public List<Player> getAllPlayers() {
        return players;
    }

    @Override
    public Optional<Player> updatePlayer(Long id, Player player) {
        for (Player p : players) {
            if (p.getId().equals(id)) {
                p.setName(player.getName());
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean deletePlayer(Long id) {
        return players.removeIf(player -> player.getId().equals(id));
    }

    // Custom methods

    @Override
    public List<Player> findByNameIgnoreCase(String name) {
        return players.stream()
                .filter(player -> player.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    public void clearPlayers() {  // method for the tearDown() test in PlayerServiceImplTest
        players.clear();
    }
}
