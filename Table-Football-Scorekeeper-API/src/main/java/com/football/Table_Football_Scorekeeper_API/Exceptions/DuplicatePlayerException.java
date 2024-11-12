package com.football.Table_Football_Scorekeeper_API.Exceptions;

public class DuplicatePlayerException extends RuntimeException {

    public DuplicatePlayerException(String message) {
        super(message);
    }

    public DuplicatePlayerException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatePlayerException() {
        super("Player's name already exists.");
    }
}
