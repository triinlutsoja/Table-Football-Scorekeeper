package com.football.Table_Football_Scorekeeper_API.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playerId;

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
