package com.football.Table_Football_Scorekeeper_API.Controllers;

import com.football.Table_Football_Scorekeeper_API.Entities.Player;
import com.football.Table_Football_Scorekeeper_API.Exceptions.EntityNotFoundException;
import com.football.Table_Football_Scorekeeper_API.Exceptions.ValidationException;
import com.football.Table_Football_Scorekeeper_API.Services.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/players")
@CrossOrigin(origins = "http://localhost:5500")  // for frontend
public class PlayerController {

    private final PlayerService playerService;
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
        return ResponseEntity.status(200).body(existingPlayer.get());
    }

    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers() {
        List<Player> allPlayers = playerService.getAllPlayers();
        return ResponseEntity.status(200).body(allPlayers);  // returns list, even if db is empty. Nothing wrong there.
    }

    @PutMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long id, @RequestBody Player player) {
        Player updatedPlayer = playerService.updatePlayer(id, player);
        return ResponseEntity.status(200).body(updatedPlayer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        boolean isDeleted = playerService.deletePlayer(id);
        return ResponseEntity.status(204).build();  // 204 No Content when deletion is successful
    }

    @GetMapping("/by-name")
    public ResponseEntity<List<Player>> findByNameIgnoreCase(@RequestParam String name) {
        List<Player> foundPlayers = playerService.findByNameIgnoreCase(name);
        return ResponseEntity.status(200).body(foundPlayers);  // returns list, even if db is empty. Nothing wrong there.
    }
}
