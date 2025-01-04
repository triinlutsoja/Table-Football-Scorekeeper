package com.football.Table_Football_Scorekeeper_API.Services;

import com.football.Table_Football_Scorekeeper_API.Entities.Game;
import com.football.Table_Football_Scorekeeper_API.Entities.PlayerStat;

import java.util.List;

public interface GameStatsService {

    List<PlayerStat> calculateWinStats();
}
