package com.football.Table_Football_Scorekeeper_API.Controllers;

import com.football.Table_Football_Scorekeeper_API.Entities.Player;
import com.football.Table_Football_Scorekeeper_API.Repositories.PlayerRepositoryImpl;
import com.football.Table_Football_Scorekeeper_API.Services.PlayerService;
import com.football.Table_Football_Scorekeeper_API.Services.PlayerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    // DI through constructor
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    public ResponseEntity<Player> addPlayer(@RequestBody Player player) {
        Optional<Player> newPlayer = playerService.addPlayer(player);
        if (newPlayer.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(201).body(newPlayer.get());
    }
/* COMMENTING OUT
    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable Long id) {
        Optional<Player> existingPlayer = playerService.getPlayer(id);
        if (existingPlayer.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(200).body(existingPlayer.get());
    }

    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers() {
        List<Player> allPlayers = playerService.getAllPlayers();
        if (allPlayers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allPlayers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long id, @RequestBody Player player) {
        Optional<Player> existingPlayer = playerService.getPlayer(id);
        if (existingPlayer.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Player updatedPlayer = playerService.updatePlayer(id, player).get();
        return ResponseEntity.ok(updatedPlayer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        Optional<Player> existingPlayer = playerService.getPlayer(id);
        if (existingPlayer.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        playerService.deletePlayer(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/by-name")
    public ResponseEntity<List<Player>> getPlayersByName(@RequestParam String name) {
        List<Player> players = playerService.getPlayersByName(name);
        if (players.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(players);
    }*/
}
