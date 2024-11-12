package com.football.Table_Football_Scorekeeper_API.Controllers;

import com.football.Table_Football_Scorekeeper_API.Entities.Player;
import com.football.Table_Football_Scorekeeper_API.Repositories.PlayerRepository;
import com.football.Table_Football_Scorekeeper_API.Services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    // Constructor to inject the repository
    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    public ResponseEntity<Player> addPlayer(@RequestBody Player player) {
        Player newPlayer = playerService.addPlayer(player);
        return ResponseEntity.status(201).body(newPlayer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable Long id) {
        Player existingplayer = playerService.getPlayer(id).get();
        return ResponseEntity.status(200).body(existingplayer);
    }

    @GetMapping  // Spring Boot automatically supports Sort parameters in the URL with the format ?sort=property,asc or ?sort=property,desc.
    public ResponseEntity<List<Player>> getAllPlayers(@RequestParam Sort sort) {
        List<Player> allPlayers = playerService.getAllPlayers(sort);
        if (allPlayers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allPlayers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long id, @RequestParam String name) {
        Optional<Player> existingPlayer = playerService.getPlayer(id);
        if (existingPlayer.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Player updatedPlayer = playerService.updatePlayer(id, name).get();
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
}
