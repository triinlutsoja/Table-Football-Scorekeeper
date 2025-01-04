package com.football.Table_Football_Scorekeeper_API.Entities;

public class PlayerStat {

    private String playerName;
    private int victoryCount;

    public PlayerStat() {}

    public PlayerStat(String playerName, int victoryCount) {
        this.playerName = playerName;
        this.victoryCount = victoryCount;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getVictoryCount() {
        return victoryCount;
    }

    public void setVictoryCount(int victoryCount) {
        this.victoryCount = victoryCount;
    }
}
