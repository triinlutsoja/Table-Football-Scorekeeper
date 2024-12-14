package com.football.Table_Football_Scorekeeper_API.Entities;

import com.football.Table_Football_Scorekeeper_API.Annotations.Entity;
import com.football.Table_Football_Scorekeeper_API.Annotations.Field;

import java.time.LocalDateTime;

@Entity(tableName = "game")
public class Game {

    @Field(isPrimaryKey = true)
    private Long id;

    @Field
    private LocalDateTime timestamp;

    @Field
    private Long greyId;

    @Field
    private Long blackId;

    @Field
    private int scoreGrey;

    @Field
    private int scoreBlack;

    public Game() {}

    public Game(Long blackId, int scoreGrey, int scoreBlack, Long greyId) {
        this.timestamp = LocalDateTime.now();  // sets the current timestamp automatically
        this.blackId = blackId;
        this.scoreGrey = scoreGrey;
        this.scoreBlack = scoreBlack;
        this.greyId = greyId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
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
