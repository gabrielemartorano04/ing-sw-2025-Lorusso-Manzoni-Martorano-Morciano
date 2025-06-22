package it.polimi.ingsw.exceptions;

/**
 * The game is already started when a player tries to log
 */
public class GameAlreadyStartedException extends RuntimeException {
    public GameAlreadyStartedException(String message) {
        super(message);
    }
}
