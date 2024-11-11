package com.football.Table_Football_Scorekeeper_API.Exceptions;

public class PlayerNotFoundException extends RuntimeException {

    public PlayerNotFoundException(String message) {
        super(message);
    }

    public PlayerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlayerNotFoundException() {
        super("Player not found.");
    }
}
