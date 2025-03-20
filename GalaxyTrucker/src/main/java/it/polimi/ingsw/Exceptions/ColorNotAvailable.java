package it.polimi.ingsw.Exceptions;

public class ColorNotAvailable extends RuntimeException {
    public ColorNotAvailable(String message) {
        super(message);
    }
}
