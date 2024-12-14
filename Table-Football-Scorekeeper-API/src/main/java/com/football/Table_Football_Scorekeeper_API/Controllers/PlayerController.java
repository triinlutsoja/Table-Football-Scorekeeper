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
        Player newPlayer = playerService.addPlayer(player);
        return ResponseEntity.status(201).body(newPlayer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable Long id) {
        Optional<Player> existingPlayer = playerService.getPlayer(id);
        if (existingPlayer.isPresent()) {
            return ResponseEntity.status(200).body(existingPlayer.get());
        }
        return ResponseEntity.status(404).build();
    }

    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers() {
        List<Player> allPlayers = playerService.getAllPlayers();
        return ResponseEntity.status(200).body(allPlayers);  // returns list, even if db is empty. Nothing wrong there.
    }

    /* COMMENTING OUT

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
    }*/

    @GetMapping("/by-name")
    public ResponseEntity<List<Player>> findByNameIgnoreCase(@RequestParam String name) {
        List<Player> foundPlayers = playerService.findByNameIgnoreCase(name);
        return ResponseEntity.status(200).body(foundPlayers);  // returns list, even if db is empty. Nothing wrong there.
    }
}
