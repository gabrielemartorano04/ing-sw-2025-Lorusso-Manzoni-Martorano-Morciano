package it.polimi.ingsw.exceptions;

/**
 * The player tried to turn the hourglass, but it's not possible at the moment
 */
public class CannotTurnHourglassException extends Exception {
    public CannotTurnHourglassException(String message) {
        super(message);
    }
}
