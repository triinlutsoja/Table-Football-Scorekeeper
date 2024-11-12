package com.football.Table_Football_Scorekeeper_API.Controllers;

import com.football.Table_Football_Scorekeeper_API.Entities.Player;
import com.football.Table_Football_Scorekeeper_API.Repositories.PlayerRepository;
import com.football.Table_Football_Scorekeeper_API.Services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/players")
public class PlayerController {

    // TODO: add private Service

    private final PlayerService playerService;

    // Constructor to inject the repository
    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    private ResponseEntity<Player> addPlayer() {
        return null;
    }

    private ResponseEntity<Player> getPlayer() {
        return null;
    }

    private ResponseEntity<Player> getAllPlayers(Sort sort) {
        // TODO: Request the Sort object from the user.
        return null;
    }

    private ResponseEntity<Player> updatePlayer() {
        return null;
    }

    private ResponseEntity<Player> deletePlayer() {
        return null;
    }
}
