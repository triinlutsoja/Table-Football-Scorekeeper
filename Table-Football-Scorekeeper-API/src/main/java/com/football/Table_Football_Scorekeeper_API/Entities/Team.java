package com.football.Table_Football_Scorekeeper_API.Entities;

import com.football.Table_Football_Scorekeeper_API.Annotations.Field;

public class Team {

    @Field(isPrimaryKey = true)
    private Long id;

    @Field
    private Long player1Id;

    @Field
    private Long player2Id;

    public Team(){}

    public Team(Long playerId1, Long playerId2) {
        this.player1Id = playerId1;
        this.player2Id = playerId2;
    }

    // To create a team with one player only
    public Team(Long singlePlayerId) {
        this.player1Id = singlePlayerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(Long player1Id) {
        this.player1Id = player1Id;
    }

    public Long getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(Long player2Id) {
        this.player2Id = player2Id;
    }
}
