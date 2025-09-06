package com.football.Table_Football_Scorekeeper_API.Repositories;

import com.football.Table_Football_Scorekeeper_API.Entities.Player;
import com.football.Table_Football_Scorekeeper_API.Exceptions.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryPlayerRepository implements PlayerRepository {

    private final List<Player> db = new ArrayList<Player>();
    private Long currentId = 1L;  // to simulate auto-increment ID

    public void clearPlayers() {  // method for the tearDown() test in PlayerServiceImplTest
        db.clear();
    }

    @Override
    public Player addPlayer(Player player) {
        player.setId(currentId++);
        db.add(player);
        return player;
    }

    @Override
    public Optional<Player> getPlayer(Long id) {
        return db.stream()
                .filter(player -> player.getId().equals(id))
                .findFirst();  // Returns Optional.empty() if not found
    }

    @Override
    public List<Player> getAllPlayers() {
        return db;
    }

    @Override
    public Player updatePlayer(Long id, Player player) {
        for (Player p : db) {
            if (p.getId().equals(id)) {
                p.setName(player.getName());
                return p;
            }
        }
        return null;
    }

    @Override
    public boolean deletePlayer(Long id) {
        return db.removeIf(player -> player.getId().equals(id));
    }

    // Custom methods

    @Override
    public List<Player> findByNameIgnoreCase(String name) {
        return db.stream()
                .filter(player -> player.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }
}
