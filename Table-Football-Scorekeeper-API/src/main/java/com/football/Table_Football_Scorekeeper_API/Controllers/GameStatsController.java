package com.football.Table_Football_Scorekeeper_API.Controllers;

import com.football.Table_Football_Scorekeeper_API.Entities.PlayerStat;
import com.football.Table_Football_Scorekeeper_API.Services.GameStatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stats")
public class GameStatsController {

    private final GameStatsService gameStatsService;

    // DI through constructor
    public GameStatsController(GameStatsService gameStatsService) {
        this.gameStatsService = gameStatsService;
    }

    @GetMapping()
    public ResponseEntity<List<PlayerStat>> calculateWinStats() {
        List<PlayerStat> gameStats = gameStatsService.calculateWinStats();
        return ResponseEntity.status(200).body(gameStats);
    }
}
