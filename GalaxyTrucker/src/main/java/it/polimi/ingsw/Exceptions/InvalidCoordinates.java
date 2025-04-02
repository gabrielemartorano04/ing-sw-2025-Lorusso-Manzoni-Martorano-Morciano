package it.polimi.ingsw.Exceptions;

public class InvalidCoordinates extends RuntimeException {
    public InvalidCoordinates(String message) {
        super(message);
    }
}
