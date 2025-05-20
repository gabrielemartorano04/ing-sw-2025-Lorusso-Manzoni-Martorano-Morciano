package it.polimi.ingsw.exceptions;

public class GameAlreadyStartedException extends RuntimeException {
    public GameAlreadyStartedException(String message) {
        super(message);
    }
}
