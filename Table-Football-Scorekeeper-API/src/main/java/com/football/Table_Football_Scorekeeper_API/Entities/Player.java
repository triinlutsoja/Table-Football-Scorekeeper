package com.football.Table_Football_Scorekeeper_API.Entities;

import com.football.Table_Football_Scorekeeper_API.Annotations.Entity;
import com.football.Table_Football_Scorekeeper_API.Annotations.Field;

@Entity(tableName = "player")
public class Player {

    @Field(isPrimaryKey = true)
    private Long playerId;

    @Field
    private String name;

    public Player() {}

    public Player(String name) {
        this.name = name;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long player_id) {
        this.playerId = player_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
