package com.football.Table_Football_Scorekeeper_API.Controllers;

import com.football.Table_Football_Scorekeeper_API.Entities.Team;
import com.football.Table_Football_Scorekeeper_API.Services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/teams")
@CrossOrigin(origins = "http://localhost:5500")  // for frontend
public class TeamController {

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    public ResponseEntity<Team> addTeam(@RequestBody Team team) {
        Team newTeam = teamService.addTeam(team);
        return ResponseEntity.status(201).body(newTeam);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeam(@PathVariable Long id) {
        Optional<Team> retrievedTeam = teamService.getTeam(id);
        return ResponseEntity.status(200).body(retrievedTeam.get());
    }

    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams() {
        List<Team> teams = teamService.getAllTeams();
        return ResponseEntity.status(200).body(teams);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable Long id, @RequestBody Team team) {
        Team updatedTeam = teamService.updateTeam(id, team);
        return ResponseEntity.status(200).body(updatedTeam);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        boolean isDeleted = teamService.deleteTeam(id);
        return ResponseEntity.status(204).build();
    }
}
