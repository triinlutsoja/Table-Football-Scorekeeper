package com.football.Table_Football_Scorekeeper_API.Services;

import com.football.Table_Football_Scorekeeper_API.Entities.PlayerStat;
import com.football.Table_Football_Scorekeeper_API.Repositories.GameRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

public class GameStatsServiceImplTest {

    private GameRepository gameRepository;
    private GameStatsServiceImpl gameStatsService;

    @BeforeEach
    public void setUp() {
        // Create a mock of GameRepository
        gameRepository = Mockito.mock(GameRepository.class);

        // Pass the mock to the service
        gameStatsService = new GameStatsServiceImpl(gameRepository);
    }

    @Test
    void calculateWinStats_ShouldReturnAListOfWinStats() {
        // Arrange
        List<PlayerStat> winnerStats = new ArrayList<>();
        PlayerStat stat1 = new PlayerStat("Janet Smith", 3);
        PlayerStat stat2 = new PlayerStat("Jane Doe", 7);
        PlayerStat stat3 = new PlayerStat("John Doe", 2);
        winnerStats.add(stat1);
        winnerStats.add(stat2);
        winnerStats.add(stat3);

        when(gameRepository.calculateWinStats()).thenReturn(winnerStats);

        // Act
        List<PlayerStat> result = gameStatsService.calculateWinStats();

        // Assert
        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals("Janet Smith", result.get(0).getPlayerName());
        Assertions.assertEquals(3, result.get(0).getVictoryCount());
        Assertions.assertEquals("Jane Doe", result.get(1).getPlayerName());
        Assertions.assertEquals(7, result.get(1).getVictoryCount());
        Assertions.assertEquals("John Doe", result.get(2).getPlayerName());
        Assertions.assertEquals(2, result.get(2).getVictoryCount());

    }
}
