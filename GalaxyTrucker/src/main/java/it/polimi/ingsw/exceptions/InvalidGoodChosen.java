package it.polimi.ingsw.exceptions;

/**
 * The good chose by the player is invalid
 */
public class InvalidGoodChosen extends Exception {
    public InvalidGoodChosen(String message) {
        super(message);
    }
}
