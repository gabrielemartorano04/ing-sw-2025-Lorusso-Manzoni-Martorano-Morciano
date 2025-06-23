package it.polimi.ingsw.exceptions;

/**
 * The player chose an invalid position in the spaceship
 */
public class InvalidPositionException extends Exception {
    public InvalidPositionException(String message) {
        super(message);
    }
}
