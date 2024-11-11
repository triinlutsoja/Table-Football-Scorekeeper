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

    // Helper methord for sorting
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
    public <S extends Player> S save(S entity) {
        if (entity.getPlayerId() == null) {
            entity.setPlayerId(currentId++);
        }
        players.add(entity);
        return entity;
    }

    @Override
    public Optional<Player> findById(Long aLong) {
        return players.stream()
                .filter(player -> player.getPlayerId().equals(aLong))
                .findFirst();
    }

    @Override
    public List<Player> findAll(Sort sort) {  // sort by a player object's field
        if (sort != null) {
            return applySorting(sort);
        }
        return new ArrayList<>(players);
    }

    @Override
    public void deleteById(Long aLong) {
        players.removeIf(player -> player.getPlayerId().equals(aLong));
    }

    @Override
    public List<Player> getPlayersByName(String name) {
        return players.stream()
                .filter(player -> player.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }
}
