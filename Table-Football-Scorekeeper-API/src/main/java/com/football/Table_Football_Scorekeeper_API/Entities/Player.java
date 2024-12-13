package com.football.Table_Football_Scorekeeper_API.Entities;

import com.football.Table_Football_Scorekeeper_API.Annotations.Entity;
import com.football.Table_Football_Scorekeeper_API.Annotations.Field;

@Entity(tableName = "player")
public class Player {

    @Field(isPrimaryKey = true)
    private Long id;

    @Field
    private String name;

    public Player() {}

    public Player(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long player_id) {
        this.id = player_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
