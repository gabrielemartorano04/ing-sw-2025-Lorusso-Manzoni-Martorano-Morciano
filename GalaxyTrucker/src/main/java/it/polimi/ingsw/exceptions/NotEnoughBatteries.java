package it.polimi.ingsw.exceptions;

/**
 * The player didn't select enough batteries
 */
public class NotEnoughBatteries extends Exception {
    public NotEnoughBatteries(String message) {
        super(message);
    }
}
