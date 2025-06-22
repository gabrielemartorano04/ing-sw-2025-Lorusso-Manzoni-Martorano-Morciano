package it.polimi.ingsw.exceptions;

/**
 * Wrong call for card controller which calls cards method
 */
public class WrongCallerException extends Exception {
    public WrongCallerException(String message) {
        super(message);
    }
}
