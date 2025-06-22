package it.polimi.ingsw.exceptions;

/**
 * The player inserted invalid coordinates
 */
public class InvalidCoordinates extends Exception {
    public InvalidCoordinates(String message) {
        super(message);
    }
}
