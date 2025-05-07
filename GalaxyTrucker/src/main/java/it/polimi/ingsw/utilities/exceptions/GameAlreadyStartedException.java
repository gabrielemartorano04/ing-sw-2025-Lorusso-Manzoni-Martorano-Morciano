package it.polimi.ingsw.utilities.exceptions;

public class GameAlreadyStartedException extends RuntimeException {
    public GameAlreadyStartedException(String message) {
        super(message);
    }
}
