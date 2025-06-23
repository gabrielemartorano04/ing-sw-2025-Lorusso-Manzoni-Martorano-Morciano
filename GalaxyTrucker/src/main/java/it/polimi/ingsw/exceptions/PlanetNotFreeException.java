package it.polimi.ingsw.exceptions;

/**
 * The selected planet is not available to land
 */
public class PlanetNotFreeException extends Exception {
    public PlanetNotFreeException(String message) {
        super(message);
    }
}
