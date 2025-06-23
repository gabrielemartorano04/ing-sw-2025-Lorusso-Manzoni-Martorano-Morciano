package it.polimi.ingsw.exceptions;

/**
 * Generic error for player data inserted
 */
public class GeneralError extends Exception {
    public GeneralError(String message) {
        super(message);
    }
}
