package com.football.Table_Football_Scorekeeper_API.Controllers;

import com.football.Table_Football_Scorekeeper_API.Entities.Game;
import com.football.Table_Football_Scorekeeper_API.Services.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    // DI through constructor
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<Game> addGame(@RequestBody Game game) {
        Game newGame = gameService.addGame(game);
        return ResponseEntity.status(200).body(newGame);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGame(@PathVariable Long id) {
        Optional<Game> existingGame = gameService.getGame(id);
        if (existingGame.isPresent()) {
            return ResponseEntity.status(200).body(existingGame.get());
        }
        return ResponseEntity.status(404).build();
    }

    @GetMapping()
    public ResponseEntity<List<Game>> getAllGames() {
        List<Game> games = gameService.getAllGames();
        return ResponseEntity.status(200).body(games);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Optional<Game>> updateGame(@PathVariable Long id, @RequestBody Game game) {
        Optional<Game> updatedGame = gameService.updateGame(id, game);
        if (updatedGame.isPresent()) {
            return ResponseEntity.status(200).body(updatedGame);
        }
        return ResponseEntity.status(404).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        if (gameService.deleteGame(id)) {
            return ResponseEntity.noContent().build();  // 204 No Content when deletion is successful
        }
        return ResponseEntity.status(404).build();
    }

}
