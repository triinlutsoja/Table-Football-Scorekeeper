package com.football.Table_Football_Scorekeeper_API.Controllers;

import com.football.Table_Football_Scorekeeper_API.Entities.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/players")
public class PlayerController {

    // TODO: add private Service

    private ResponseEntity<Player> addPlayer() {
        return null;
    }

    private ResponseEntity<Player> getPlayer() {
        return null;
    }

    private ResponseEntity<Player> getAllPlayers(){
        return null;
    }

    private ResponseEntity<Player> updatePlayer() {
        return null;
    }

    private ResponseEntity<Player> deletePlayer() {
        return null;
    }
}
