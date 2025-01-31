package com.football.Table_Football_Scorekeeper_API.Services;

import com.football.Table_Football_Scorekeeper_API.Entities.Game;
import com.football.Table_Football_Scorekeeper_API.Entities.PlayerStat;
import com.football.Table_Football_Scorekeeper_API.Repositories.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameStatsServiceImpl implements GameStatsService {

    private final GameRepository gameRepository;

    public GameStatsServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public List<PlayerStat> calculateWinStats() {
       return gameRepository.calculateWinStats();
    }
}
