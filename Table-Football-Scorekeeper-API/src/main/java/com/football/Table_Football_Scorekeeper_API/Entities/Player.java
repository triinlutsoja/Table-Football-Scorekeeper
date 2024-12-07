package com.football.Table_Football_Scorekeeper_API.Entities;

import jakarta.persistence.*;

public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playerId;

    @Column(nullable = false, unique = true) // Name cannot be null and must be unique
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
