package com.football.Table_Football_Scorekeeper_API.Entities;

public class Game {

    private Long gameId;

    private Long greyId;

    private Long blackId;

    private int scoreGrey;

    private int scoreBlack;

    public Game() {}

    public Game(Long greyId, Long blackId, int scoreGrey, int scoreBlack) {
        this.greyId = greyId;
        this.blackId = blackId;
        this.scoreGrey = scoreGrey;
        this.scoreBlack = scoreBlack;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getGreyId() {
        return greyId;
    }

    public void setGreyId(Long greyId) {
        this.greyId = greyId;
    }

    public Long getBlackId() {
        return blackId;
    }

    public void setBlackId(Long blackId) {
        this.blackId = blackId;
    }

    public int getScoreGrey() {
        return scoreGrey;
    }

    public void setScoreGrey(int scoreGrey) {
        this.scoreGrey = scoreGrey;
    }

    public int getScoreBlack() {
        return scoreBlack;
    }

    public void setScoreBlack(int scoreBlack) {
        this.scoreBlack = scoreBlack;
    }
}
