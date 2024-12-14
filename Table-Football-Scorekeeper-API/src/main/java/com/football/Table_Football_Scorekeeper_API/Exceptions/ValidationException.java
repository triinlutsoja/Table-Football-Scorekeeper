package com.football.Table_Football_Scorekeeper_API.Exceptions;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException() {
        super("Invalid input.");
    }
}
