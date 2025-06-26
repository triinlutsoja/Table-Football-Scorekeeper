package com.football.Table_Football_Scorekeeper_API.Services;

import com.football.Table_Football_Scorekeeper_API.Entities.Team;

import java.util.List;
import java.util.Optional;

public interface TeamService {

    Team addTeam(Team team);
    Optional<Team> getTeam(Long id);
    List<Team> getAllTeams();
    Team updateTeam(Long id, Team team);
    boolean deleteTeam(Long id);
}
