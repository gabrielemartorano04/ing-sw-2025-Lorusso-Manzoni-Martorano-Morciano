package it.polimi.ingsw.exceptions;

/**
 * The chosen color is not available for login
 */
public class ColorNotAvailable extends Exception {
    public ColorNotAvailable(String message) {
        super(message);
    }
}
