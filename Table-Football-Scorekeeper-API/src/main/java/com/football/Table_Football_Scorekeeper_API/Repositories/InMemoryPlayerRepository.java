package com.football.Table_Football_Scorekeeper_API.Repositories;

import com.football.Table_Football_Scorekeeper_API.Entities.Player;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryPlayerRepository implements PlayerRepository {

    private final List<Player> players = new ArrayList<Player>();
    private Long currentId = 0L;  // to simulate auto-increment ID

    // Helper methord for sorting
    private List<Player> applySorting(Sort sort) {  // TODO: sa ei saanud veel päris sellest meetodist aru
        // Convert Sort object into a comparator to sort the list
        List<Player> sortedPlayers = new ArrayList<>(players);  // copy of players to keep the original unaltered

        sort.forEach(order -> {
            if (order.getProperty().equals("name")) {
                if sortedPlayers.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
            } else {
                sortedPlayers.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
            }
        // Add more sorting conditions for other properties (e.g., playerId)
            else if (order.getProperty().equals("playerId")) {
                if (order.isAscending()) {
                    sortedPlayers.sort((p1, p2) -> p1.getPlayerId().compareTo(p2.getPlayerId()));
                } else {
                    sortedPlayers.sort((p1, p2) -> p2.getPlayerId().compareTo(p1.getPlayerId()));
                }
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
            return null; // TODO: Siin jäi sul pooleli seoses applySorting meetodiga.
        }
        return players;
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
